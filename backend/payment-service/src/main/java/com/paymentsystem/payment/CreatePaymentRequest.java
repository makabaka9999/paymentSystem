package com.paymentsystem.payment;

import java.math.BigDecimal;

/**
 * 创建支付单请求参数。
 */
public class CreatePaymentRequest {
    /** 订单 ID。 */
    private Long orderId;
    /** 支付用户 ID。 */
    private Long userId;
    /** 支付金额。 */
    private BigDecimal amount;
    /** 客户端幂等键。 */
    private String idempotencyKey;

    /** @return 订单 ID */
    public Long getOrderId() { return orderId; }
    /** @param orderId 订单 ID */
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    /** @return 支付用户 ID */
    public Long getUserId() { return userId; }
    /** @param userId 支付用户 ID */
    public void setUserId(Long userId) { this.userId = userId; }
    /** @return 支付金额，未传时返回 0 */
    public BigDecimal getAmount() { return amount == null ? BigDecimal.ZERO : amount; }
    /** @param amount 支付金额 */
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    /** @return 客户端幂等键 */
    public String getIdempotencyKey() { return idempotencyKey; }
    /** @param idempotencyKey 客户端幂等键 */
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
}
