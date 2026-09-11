package com.sky.mq;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderCloseOutboxSqlContractTest {

    @Test
    void outboxDdlHasUniqueMessageAndOrderKeys() throws Exception {
        String ddl = read("docs/sql/order-close-outbox.sql");

        assertTrue(ddl.contains("UNIQUE KEY uk_message_id (message_id)"));
        assertTrue(ddl.contains("UNIQUE KEY uk_order_id (order_id)"));
    }

    @Test
    void mapperSelectsOnlyDuePendingRows() throws Exception {
        String mapper = read("sky-take-out/sky-server/src/main/resources/mapper/OrderCloseOutboxMapper.xml");

        assertTrue(mapper.contains("status = 0"));
        assertTrue(mapper.contains("next_retry_time &lt;= #{now}"));
        assertTrue(mapper.contains("limit #{limit}"));
    }

    private String read(String relativePath) throws Exception {
        Path root = Paths.get("").toAbsolutePath();
        while (root != null && !Files.exists(root.resolve(relativePath))) {
            root = root.getParent();
        }
        if (root == null) {
            throw new IllegalStateException("Could not find repository file: " + relativePath);
        }
        Path path = root.resolve(relativePath);
        byte[] content = Files.readAllBytes(path);
        return new String(content, StandardCharsets.UTF_8);
    }
}
