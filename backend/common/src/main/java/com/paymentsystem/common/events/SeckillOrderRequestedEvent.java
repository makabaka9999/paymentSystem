package com.paymentsystem.common.events;

import java.time.Instant;

public class SeckillOrderRequestedEvent {
    private String requestId;
    private Long activityId;
    private Long userId;
    private Long skuId;
    private Integer quantity;
    private Instant requestedAt;

    public SeckillOrderRequestedEvent() {
    }

    public SeckillOrderRequestedEvent(String requestId, Long activityId, Long userId, Long skuId, Integer quantity, Instant requestedAt) {
        this.requestId = requestId;
        this.activityId = activityId;
        this.userId = userId;
        this.skuId = skuId;
        this.quantity = quantity;
        this.requestedAt = requestedAt;
    }
}
