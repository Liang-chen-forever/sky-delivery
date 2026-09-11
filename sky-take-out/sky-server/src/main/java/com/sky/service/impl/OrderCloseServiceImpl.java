package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderCloseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 延迟关单服务实现：通过条件更新保证关单与支付回调并发时的原子性
 */
@Service
public class OrderCloseServiceImpl implements OrderCloseService {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderCloseServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public int closeIfPending(Long orderId) {
        return orderMapper.closeIfPending(
                orderId,
                Orders.PENDING_PAYMENT,
                Orders.UN_PAID,
                Orders.CANCELLED,
                "支付超时，自动取消",
                LocalDateTime.now());
    }
}
