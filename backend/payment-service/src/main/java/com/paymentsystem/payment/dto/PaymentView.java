package com.paymentsystem.payment.dto;

import com.paymentsystem.common.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 支付单响应视图。
 */
public class PaymentView {
    /** 支付单号。 */
    private String paymentNo;
    /** 订单 ID。 */
    private Long orderId;
    /** 支付用户 ID。 */
    private Long userId;
    /** 支付金额。 */
    private BigDecimal amount;
    /** 支付状态。 */
    private PaymentStatus status;
    /** 支付单创建时间。 */
    private Instant createdAt;
    /** 支付完成时间。 */
    private Instant paidAt;
    /** 创建支付单使用的幂等键。 */
    private String idempotencyKey;

    /**
     * 创建支付单视图。
     */
    public PaymentView(String paymentNo, Long orderId, Long userId, BigDecimal amount, PaymentStatus status, Instant createdAt, Instant paidAt, String idempotencyKey) {
        this.paymentNo = paymentNo; this.orderId = orderId; this.userId = userId; this.amount = amount; this.status = status; this.createdAt = createdAt; this.paidAt = paidAt; this.idempotencyKey = idempotencyKey;
    }

    /**
     * 复制当前支付单并替换状态。
     *
     * @param nextStatus 新支付状态
     * @return 新支付单视图
     */
    public PaymentView withStatus(PaymentStatus nextStatus) { return new PaymentView(paymentNo, orderId, userId, amount, nextStatus, createdAt, nextStatus == PaymentStatus.SUCCESS ? Instant.now() : paidAt, idempotencyKey); }

    /** @return 支付单号 */
    public String getPaymentNo() { return paymentNo; }
    /** @return 订单 ID */
    public Long getOrderId() { return orderId; }
    /** @return 支付用户 ID */
    public Long getUserId() { return userId; }
    /** @return 支付金额 */
    public BigDecimal getAmount() { return amount; }
    /** @return 支付状态 */
    public PaymentStatus getStatus() { return status; }
    /** @return 支付单创建时间 */
    public Instant getCreatedAt() { return createdAt; }
    /** @return 支付完成时间 */
    public Instant getPaidAt() { return paidAt; }
    /** @return 创建支付单使用的幂等键 */
    public String getIdempotencyKey() { return idempotencyKey; }
}
