CREATE TABLE order_close_outbox (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id VARCHAR(64) NOT NULL COMMENT '消息唯一标识',
    order_id BIGINT NOT NULL COMMENT '订单主键',
    payload VARCHAR(64) NOT NULL COMMENT '消息载荷，仅保存订单ID',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0待发送 1已发送',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '发布重试次数',
    next_retry_time DATETIME NOT NULL COMMENT '下次可发布时间',
    last_error VARCHAR(512) NULL COMMENT '最近一次发布错误',
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL,
    UNIQUE KEY uk_message_id (message_id),
    UNIQUE KEY uk_order_id (order_id),
    KEY idx_pending_retry (status, next_retry_time, id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单延迟关单本地消息表';
