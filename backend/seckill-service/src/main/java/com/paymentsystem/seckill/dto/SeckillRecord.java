package com.paymentsystem.seckill.dto;

import java.time.Instant;

/**
 * 秒杀请求记录。
 */
public class SeckillRecord {
    /** 秒杀请求 ID。 */
    private String requestId;
    /** 活动 ID。 */
    private Long activityId;
    /** 用户 ID。 */
    private Long userId;
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 请求处理状态。 */
    private String status;
    /** 请求处理消息。 */
    private String message;
    /** 请求创建时间。 */
    private Instant createdAt;

    /**
     * 创建秒杀请求记录。
     */
    public SeckillRecord(String requestId, Long activityId, Long userId, Long skuId, String status, String message, Instant createdAt) {
        this.requestId = requestId; this.activityId = activityId; this.userId = userId; this.skuId = skuId; this.status = status; this.message = message; this.createdAt = createdAt;
    }

    /** @return 秒杀请求 ID */
    public String getRequestId() { return requestId; }
    /** @return 活动 ID */
    public Long getActivityId() { return activityId; }
    /** @return 用户 ID */
    public Long getUserId() { return userId; }
    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @return 请求处理状态 */
    public String getStatus() { return status; }
    /** @return 请求处理消息 */
    public String getMessage() { return message; }
    /** @return 请求创建时间 */
    public Instant getCreatedAt() { return createdAt; }
}
