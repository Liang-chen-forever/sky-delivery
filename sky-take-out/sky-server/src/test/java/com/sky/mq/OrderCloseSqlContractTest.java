package com.sky.mq;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderCloseSqlContractTest {

    @Test
    void closeUpdateRequiresPendingAndUnpaidState() throws Exception {
        String mapper = read("sky-take-out/sky-server/src/main/resources/mapper/OrderMapper.xml");

        assertTrue(mapper.contains("id = #{id}"));
        assertTrue(mapper.contains("status = #{pendingStatus}"));
        assertTrue(mapper.contains("pay_status = #{unpaidStatus}"));
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
    }
}
