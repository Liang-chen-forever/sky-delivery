package com.sky.mq;

/** RabbitMQ names used by the delayed order-close flow. */
public final class OrderMqConstant {

    public static final String DELAY_EXCHANGE = "order.delay.exchange";
    public static final String DELAY_QUEUE = "order.delay.queue";
    public static final String DELAY_ROUTING_KEY = "order.delay";

    public static final String CLOSE_EXCHANGE = "order.close.exchange";
    public static final String DLX_EXCHANGE = CLOSE_EXCHANGE;
    public static final String CLOSE_QUEUE = "order.close.queue";
    public static final String CLOSE_ROUTING_KEY = "order.close";

    private OrderMqConstant() {
    }
}
