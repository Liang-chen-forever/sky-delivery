package com.sky.mq;

import com.sky.service.OrderCloseService;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OrderCloseConsumerTest {

    @Test
    void successfulCloseAcknowledgesTheMessage() throws IOException {
        OrderCloseService service = mock(OrderCloseService.class);
        Channel channel = mock(Channel.class);
        Message message = messageWithTag(11L);

        new OrderCloseConsumer(service).onCloseOrder(42L, message, channel);

        verify(service).closeIfPending(42L);
        verify(channel).basicAck(11L, false);
    }

    @Test
    void failedCloseRequeuesTheMessage() throws IOException {
        OrderCloseService service = mock(OrderCloseService.class);
        Channel channel = mock(Channel.class);
        Message message = messageWithTag(12L);
        doThrow(new IllegalStateException("database unavailable"))
                .when(service).closeIfPending(42L);

        new OrderCloseConsumer(service).onCloseOrder(42L, message, channel);

        verify(channel).basicNack(12L, false, true);
    }

    private Message messageWithTag(long deliveryTag) {
        MessageProperties properties = new MessageProperties();
        properties.setDeliveryTag(deliveryTag);
        return new Message(new byte[0], properties);
    }
}
