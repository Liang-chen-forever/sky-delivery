package com.sky.mq;

import com.sky.entity.OrderCloseOutbox;
import com.sky.mapper.OrderCloseOutboxMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OrderCloseProducerTest {

    @Test
    void createMessageWritesAPendingOutboxRecordForTheOrder() {
        OrderCloseOutboxMapper mapper = mock(OrderCloseOutboxMapper.class);

        new OrderCloseProducer(mapper).createMessage(42L);

        org.mockito.ArgumentCaptor<OrderCloseOutbox> captor =
                org.mockito.ArgumentCaptor.forClass(OrderCloseOutbox.class);
        verify(mapper).insert(captor.capture());

        OrderCloseOutbox outbox = captor.getValue();
        assertEquals(42L, outbox.getOrderId());
        assertEquals("42", outbox.getPayload());
        assertEquals(OrderCloseOutbox.PENDING, outbox.getStatus());
        assertEquals(0, outbox.getRetryCount());
        assertNotNull(outbox.getMessageId());
        assertNotNull(outbox.getNextRetryTime());
        assertNotNull(outbox.getCreateTime());
        assertNotNull(outbox.getUpdateTime());
    }
}
