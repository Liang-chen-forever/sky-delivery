package com.sky.mq;

import com.sky.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RabbitMQConfigTest {

    @Test
    void delayQueueUsesConfiguredTtlAndDeadLetterRoute() {
        RabbitMQConfig config = new RabbitMQConfig(15);

        Queue queue = config.delayQueue();

        assertEquals(15 * 60 * 1000L, queue.getArguments().get("x-message-ttl"));
        assertEquals(OrderMqConstant.DLX_EXCHANGE, queue.getArguments().get("x-dead-letter-exchange"));
        assertEquals(OrderMqConstant.CLOSE_ROUTING_KEY, queue.getArguments().get("x-dead-letter-routing-key"));
        assertTrue(queue.isDurable());
    }

    @Test
    void closeQueueIsDurable() {
        RabbitMQConfig config = new RabbitMQConfig(15);

        assertTrue(config.closeQueue().isDurable());
    }

    @Test
    void devExampleExposesAllOrderCloseSettings() throws Exception {
        String config = read("sky-take-out/sky-server/src/main/resources/application-dev.example.yml");

        assertTrue(config.contains("timeout-minutes: ${SKY_ORDER_TIMEOUT_MINUTES:15}"));
        assertTrue(config.contains("outbox-batch-size: ${SKY_ORDER_OUTBOX_BATCH_SIZE:100}"));
        assertTrue(config.contains("outbox-relay-delay-ms: ${SKY_ORDER_OUTBOX_RELAY_DELAY_MS:1000}"));
        assertTrue(config.contains("outbox-retry-delay-seconds: ${SKY_ORDER_OUTBOX_RETRY_DELAY_SECONDS:30}"));
        assertTrue(config.contains("publisher-confirm-timeout-ms: ${SKY_ORDER_PUBLISHER_CONFIRM_TIMEOUT_MS:5000}"));
    }

    private String read(String relativePath) throws Exception {
        Path root = Paths.get("").toAbsolutePath();
        while (root != null && !Files.exists(root.resolve(relativePath))) {
            root = root.getParent();
        }
        if (root == null) {
            throw new IllegalStateException("Could not find repository file: " + relativePath);
        }
        return new String(Files.readAllBytes(root.resolve(relativePath)), StandardCharsets.UTF_8);
    }}
