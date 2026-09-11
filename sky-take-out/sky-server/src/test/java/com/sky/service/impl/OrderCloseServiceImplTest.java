package com.sky.service.impl;

import com.sky.mapper.OrderMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderCloseServiceImplTest {

    @Test
    void closeIfPendingDelegatesToTheConditionalOrderUpdate() {
        OrderMapper mapper = mock(OrderMapper.class);
        when(mapper.closeIfPending(eq(42L), any(), any(), any(), any(), any())).thenReturn(1);

        OrderCloseServiceImpl service = new OrderCloseServiceImpl(mapper);

        assertEquals(1, service.closeIfPending(42L));
        verify(mapper).closeIfPending(eq(42L), eq(1), eq(0), eq(6), any(), any());
    }
}
