package com.sky.mq;

import com.sky.entity.OrderCloseOutbox;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class OrderCloseMessagePublisherTest {

    @Test
    void negativePublisherConfirmFailsThePublish() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        doAnswer(invocation -> {
            CorrelationData correlationData = invocation.getArgument(3);
            correlationData.getFuture().set(new CorrelationData.Confirm(false, "exchange rejected"));
            return null;
        }).when(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(Object.class),
                any(CorrelationData.class));

        OrderCloseMessagePublisher publisher = new OrderCloseMessagePublisher(rabbitTemplate, 1000);

        assertThrows(IllegalStateException.class, () -> publisher.publish(outbox()));
    }

    @org.junit.jupiter.api.Test
    void positivePublisherConfirmCompletesThePublish() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        doAnswer(invocation -> {
            CorrelationData correlationData = invocation.getArgument(3);
            correlationData.getFuture().set(new CorrelationData.Confirm(true, null));
            return null;
        }).when(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(Object.class),
                any(CorrelationData.class));

        OrderCloseMessagePublisher publisher = new OrderCloseMessagePublisher(rabbitTemplate, 1000);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> publisher.publish(outbox()));
    }
    private OrderCloseOutbox outbox() {
        return OrderCloseOutbox.builder()
                .messageId("message-7")
                .orderId(42L)
                .payload("42")
                .build();
    }
}
