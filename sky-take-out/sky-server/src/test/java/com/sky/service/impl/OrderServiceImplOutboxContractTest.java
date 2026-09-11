package com.sky.service.impl;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 源码契约测试：下单事务内写入outbox记录，且旧的待支付超时扫描定时任务已被移除
 */
class OrderServiceImplOutboxContractTest {

    @Test
    void submitOrderWritesOutboxRecordInsideTheOrderTransaction() throws Exception {
        String source = read("sky-take-out/sky-server/src/main/java/com/sky/service/impl/OrderServiceImpl.java");

        assertTrue(source.contains("OrderCloseProducer"));
        int insert = source.indexOf("orderMapper.insert(orders);");
        int createMessage = source.indexOf("orderCloseProducer.createMessage(orders.getId());");
        assertTrue(insert >= 0 && createMessage > insert,
                "submitOrder必须在订单插入之后、同一事务内写入关单outbox记录");
    }

    @Test
    void pendingPaymentTimeoutScanIsRemovedButDeliveryTaskRemains() throws Exception {
        String source = read("sky-take-out/sky-server/src/main/java/com/sky/task/OrderTask.java");

        assertTrue(!source.contains("processTimeoutOrder"),
                "旧的待支付超时扫描定时任务应被移除");
        assertTrue(source.contains("processDeliveryOrder"),
                "派送中订单定时任务必须保留");
    }

    private String read(String relativePath) throws IOException {
        Path root = Paths.get("").toAbsolutePath();
        while (root != null && !Files.exists(root.resolve(relativePath))) {
            root = root.getParent();
        }
        if (root == null) {
            throw new IllegalStateException("Could not find repository file: " + relativePath);
        }
        return new String(Files.readAllBytes(root.resolve(relativePath)), StandardCharsets.UTF_8);
    }
}
