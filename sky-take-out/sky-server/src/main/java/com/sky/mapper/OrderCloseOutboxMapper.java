package com.sky.mapper;

import com.sky.entity.OrderCloseOutbox;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderCloseOutboxMapper {

    void insert(OrderCloseOutbox outbox);

    List<OrderCloseOutbox> findPending(@Param("now") LocalDateTime now, @Param("limit") int limit);

    int markPublished(@Param("id") Long id, @Param("updateTime") LocalDateTime updateTime);

    int markRetry(@Param("id") Long id,
                  @Param("retryCount") int retryCount,
                  @Param("nextRetryTime") LocalDateTime nextRetryTime,
                  @Param("lastError") String lastError,
                  @Param("updateTime") LocalDateTime updateTime);
}
