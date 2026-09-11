package com.sky.mq;

import com.rabbitmq.client.Channel;
import com.sky.service.OrderCloseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 延迟关单消费者：消费死信队列中到达超时时间的订单消息，手动ack保证处理失败可重试
 */
@Component
public class OrderCloseConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderCloseConsumer.class);

    private final OrderCloseService orderCloseService;

    public OrderCloseConsumer(OrderCloseService orderCloseService) {
        this.orderCloseService = orderCloseService;
    }

    /**
     * 关单逻辑失败（如数据库不可用）时nack重新入队；
     * 关单条件不满足（返回0，说明已支付）属于正常业务结果，直接ack
     */
    @RabbitListener(queues = OrderMqConstant.CLOSE_QUEUE)
    public void onCloseOrder(Long orderId, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            int closed = orderCloseService.closeIfPending(orderId);
            if (closed == 0) {
                log.info("订单{}已不满足关单条件（可能已支付），跳过关单", orderId);
            }
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("订单{}关单处理失败，消息重新入队", orderId, e);
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
