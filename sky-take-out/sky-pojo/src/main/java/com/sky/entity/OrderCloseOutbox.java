package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/** Transactional outbox record for a delayed order-close message. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCloseOutbox implements Serializable {

    public static final int PENDING = 0;
    public static final int PUBLISHED = 1;

    private Long id;
    private String messageId;
    private Long orderId;
    private String payload;
    private Integer status;
    private Integer retryCount;
    private LocalDateTime nextRetryTime;
    private String lastError;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
