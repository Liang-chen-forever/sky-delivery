package com.sky.service;

/**
 * 延迟关单服务
 */
public interface OrderCloseService {

    /**
     * 仅当订单仍为待支付且未支付时将其取消
     * @param orderId 订单id
     * @return 影响行数，0表示订单已不满足关单条件（如已支付）
     */
    int closeIfPending(Long orderId);
}
