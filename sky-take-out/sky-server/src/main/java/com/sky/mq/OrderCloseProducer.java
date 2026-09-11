package com.sky.mq;

import com.sky.entity.OrderCloseOutbox;
import com.sky.mapper.OrderCloseOutboxMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class OrderCloseProducer {

    private final OrderCloseOutboxMapper outboxMapper;

    @Autowired
    public OrderCloseProducer(OrderCloseOutboxMapper outboxMapper) {
        this.outboxMapper = outboxMapper;
    }

    /** Creates the durable publish intent inside the order transaction. */
    public void createMessage(Long orderId) {
        LocalDateTime now = LocalDateTime.now();
        outboxMapper.insert(OrderCloseOutbox.builder()
                .messageId(UUID.randomUUID().toString())
                .orderId(orderId)
                .payload(String.valueOf(orderId))
                .status(OrderCloseOutbox.PENDING)
                .retryCount(0)
                .nextRetryTime(now)
                .createTime(now)
                .updateTime(now)
                .build());
    }
}
