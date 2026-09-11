package com.sky.config;

import com.sky.mq.OrderMqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    private final long timeoutMinutes;

    public RabbitMQConfig(@Value("${sky.order.timeout-minutes:15}") long timeoutMinutes) {
        if (timeoutMinutes <= 0) {
            throw new IllegalArgumentException("sky.order.timeout-minutes must be positive");
        }
        this.timeoutMinutes = timeoutMinutes;
    }

    @Bean
    public Queue delayQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", timeoutMinutes * 60 * 1000L);
        arguments.put("x-dead-letter-exchange", OrderMqConstant.CLOSE_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", OrderMqConstant.CLOSE_ROUTING_KEY);
        return new Queue(OrderMqConstant.DELAY_QUEUE, true, false, false, arguments);
    }

    @Bean
    public Queue closeQueue() {
        return new Queue(OrderMqConstant.CLOSE_QUEUE, true);
    }

    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(OrderMqConstant.DELAY_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange closeExchange() {
        return new DirectExchange(OrderMqConstant.CLOSE_EXCHANGE, true, false);
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue())
                .to(delayExchange())
                .with(OrderMqConstant.DELAY_ROUTING_KEY);
    }

    @Bean
    public Binding closeBinding() {
        return BindingBuilder.bind(closeQueue())
                .to(closeExchange())
                .with(OrderMqConstant.CLOSE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
