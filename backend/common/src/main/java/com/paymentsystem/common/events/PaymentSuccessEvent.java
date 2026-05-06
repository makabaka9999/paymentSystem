package com.paymentsystem.common.events;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentSuccessEvent {
    private String paymentNo;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private Instant paidAt;
    private String idempotencyKey;

    public PaymentSuccessEvent() {
    }

    public PaymentSuccessEvent(String paymentNo, Long orderId, Long userId, BigDecimal amount, Instant paidAt, String idempotencyKey) {
        this.paymentNo = paymentNo;
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paidAt = paidAt;
        this.idempotencyKey = idempotencyKey;
    }
}
