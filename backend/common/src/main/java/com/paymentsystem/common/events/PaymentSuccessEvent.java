package com.paymentsystem.common.events;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 支付成功领域事件。
 */
public class PaymentSuccessEvent {
    /** 支付单号。 */
    private String paymentNo;
    /** 订单 ID。 */
    private Long orderId;
    /** 支付用户 ID。 */
    private Long userId;
    /** 支付金额。 */
    private BigDecimal amount;
    /** 支付完成时间。 */
    private Instant paidAt;
    /** 创建支付单时使用的幂等键。 */
    private String idempotencyKey;

    /**
     * 创建空事件对象，供序列化框架使用。
     */
    public PaymentSuccessEvent() {
    }

    /**
     * 创建支付成功事件。
     */
    public PaymentSuccessEvent(String paymentNo, Long orderId, Long userId, BigDecimal amount, Instant paidAt, String idempotencyKey) {
        this.paymentNo = paymentNo;
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paidAt = paidAt;
        this.idempotencyKey = idempotencyKey;
    }
}
