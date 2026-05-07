package com.paymentsystem.common.events;

import java.time.Instant;

/**
 * 秒杀下单请求领域事件。
 */
public class SeckillOrderRequestedEvent {
    /** 秒杀请求 ID。 */
    private String requestId;
    /** 秒杀活动 ID。 */
    private Long activityId;
    /** 用户 ID。 */
    private Long userId;
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 下单数量。 */
    private Integer quantity;
    /** 请求创建时间。 */
    private Instant requestedAt;

    /**
     * 创建空事件对象，供序列化框架使用。
     */
    public SeckillOrderRequestedEvent() {
    }

    /**
     * 创建秒杀下单请求事件。
     */
    public SeckillOrderRequestedEvent(String requestId, Long activityId, Long userId, Long skuId, Integer quantity, Instant requestedAt) {
        this.requestId = requestId;
        this.activityId = activityId;
        this.userId = userId;
        this.skuId = skuId;
        this.quantity = quantity;
        this.requestedAt = requestedAt;
    }
}
