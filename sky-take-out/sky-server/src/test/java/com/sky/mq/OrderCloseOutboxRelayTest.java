package com.sky.mq;

import com.sky.entity.OrderCloseOutbox;
import com.sky.mapper.OrderCloseOutboxMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderCloseOutboxRelayTest {

    @Test
    void successfulPublishMarksOutboxAsPublished() {
        OrderCloseOutboxMapper mapper = mock(OrderCloseOutboxMapper.class);
        OrderCloseMessagePublisher publisher = mock(OrderCloseMessagePublisher.class);
        OrderCloseOutbox outbox = pendingOutbox();
        when(mapper.findPending(any(LocalDateTime.class), eq(100)))
                .thenReturn(Collections.singletonList(outbox));

        new OrderCloseOutboxRelay(mapper, publisher, 100, 30)
                .publishPendingMessages();

        verify(publisher).publish(outbox);
        verify(mapper).markPublished(eq(7L), any(LocalDateTime.class));
    }

    @Test
    void failedPublishSchedulesTheOutboxForRetry() {
        OrderCloseOutboxMapper mapper = mock(OrderCloseOutboxMapper.class);
        OrderCloseMessagePublisher publisher = mock(OrderCloseMessagePublisher.class);
        OrderCloseOutbox outbox = pendingOutbox();
        when(mapper.findPending(any(LocalDateTime.class), eq(100)))
                .thenReturn(Collections.singletonList(outbox));
        doThrow(new IllegalStateException("broker unavailable"))
                .when(publisher).publish(outbox);

        new OrderCloseOutboxRelay(mapper, publisher, 100, 30)
                .publishPendingMessages();

        verify(mapper).markRetry(eq(7L), eq(1), any(LocalDateTime.class),
                eq("broker unavailable"), any(LocalDateTime.class));
    }

    private OrderCloseOutbox pendingOutbox() {
        return OrderCloseOutbox.builder()
                .id(7L)
                .messageId("message-7")
                .orderId(42L)
                .payload("42")
                .status(OrderCloseOutbox.PENDING)
                .retryCount(0)
                .nextRetryTime(LocalDateTime.now())
                .build();
    }
}
