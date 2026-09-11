package com.sky.mq;

import com.sky.entity.OrderCloseOutbox;
import com.sky.mapper.OrderCloseOutboxMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderCloseOutboxRelay {

    private final OrderCloseOutboxMapper outboxMapper;
    private final OrderCloseMessagePublisher publisher;
    private final int batchSize;
    private final long retryDelaySeconds;

    public OrderCloseOutboxRelay(OrderCloseOutboxMapper outboxMapper,
                                 OrderCloseMessagePublisher publisher,
                                 @Value("${sky.order.outbox-batch-size:100}") int batchSize,
                                 @Value("${sky.order.outbox-retry-delay-seconds:30}") long retryDelaySeconds) {
        if (batchSize <= 0 || retryDelaySeconds <= 0) {
            throw new IllegalArgumentException("Outbox relay settings must be positive");
        }
        this.outboxMapper = outboxMapper;
        this.publisher = publisher;
        this.batchSize = batchSize;
        this.retryDelaySeconds = retryDelaySeconds;
    }

    @Scheduled(fixedDelayString = "${sky.order.outbox-relay-delay-ms:1000}")
    public void publishPendingMessages() {
        LocalDateTime now = LocalDateTime.now();
        List<OrderCloseOutbox> pending = outboxMapper.findPending(now, batchSize);
        if (pending == null) {
            return;
        }
        for (OrderCloseOutbox outbox : pending) {
            try {
                publisher.publish(outbox);
                outboxMapper.markPublished(outbox.getId(), LocalDateTime.now());
            } catch (RuntimeException e) {
                int retryCount = (outbox.getRetryCount() == null ? 0 : outbox.getRetryCount()) + 1;
                outboxMapper.markRetry(
                        outbox.getId(),
                        retryCount,
                        LocalDateTime.now().plusSeconds(retryDelaySeconds),
                        errorMessage(e),
                        LocalDateTime.now());
            }
        }
    }

    private String errorMessage(RuntimeException exception) {
        String message = exception.getMessage();
        if (message == null || message.isEmpty()) {
            message = exception.getClass().getSimpleName();
        }
        return message.length() <= 512 ? message : message.substring(0, 512);
    }
}
