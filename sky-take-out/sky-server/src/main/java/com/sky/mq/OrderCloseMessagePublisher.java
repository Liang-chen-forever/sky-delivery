package com.sky.mq;

import com.sky.entity.OrderCloseOutbox;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class OrderCloseMessagePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final long confirmTimeoutMillis;

    public OrderCloseMessagePublisher(RabbitTemplate rabbitTemplate,
                                      @Value("${sky.order.publisher-confirm-timeout-ms:5000}") long confirmTimeoutMillis) {
        if (confirmTimeoutMillis <= 0) {
            throw new IllegalArgumentException("sky.order.publisher-confirm-timeout-ms must be positive");
        }
        this.rabbitTemplate = rabbitTemplate;
        this.confirmTimeoutMillis = confirmTimeoutMillis;
    }

    public void publish(OrderCloseOutbox outbox) {
        CorrelationData correlationData = new CorrelationData(outbox.getMessageId());
        rabbitTemplate.convertAndSend(
                OrderMqConstant.DELAY_EXCHANGE,
                OrderMqConstant.DELAY_ROUTING_KEY,
                outbox.getOrderId(),
                correlationData);

        try {
            CorrelationData.Confirm confirm = correlationData.getFuture()
                    .get(confirmTimeoutMillis, TimeUnit.MILLISECONDS);
            if (confirm == null || !confirm.isAck()) {
                String reason = confirm == null ? "no publisher confirm" : confirm.getReason();
                throw new IllegalStateException("RabbitMQ publisher confirm rejected: " + reason);
            }
            if (correlationData.getReturnedMessage() != null) {
                throw new IllegalStateException("RabbitMQ returned the order-close message");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for RabbitMQ publisher confirm", e);
        } catch (ExecutionException | TimeoutException e) {
            throw new IllegalStateException("RabbitMQ publisher confirm was not received", e);
        }
    }
}
