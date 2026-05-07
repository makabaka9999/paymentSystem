package com.paymentsystem.order;

import com.paymentsystem.common.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 订单响应视图。
 */
public class OrderView {
    /** 订单 ID。 */
    private Long id;
    /** 下单用户 ID。 */
    private Long userId;
    /** 订单商品明细列表。 */
    private List<OrderItem> items;
    /** 订单总金额。 */
    private BigDecimal totalAmount;
    /** 订单当前状态。 */
    private OrderStatus status;
    /** 订单创建时间。 */
    private Instant createdAt;
    /** 订单支付时间。 */
    private Instant paidAt;

    /**
     * 创建订单响应视图。
     */
    public OrderView(Long id, Long userId, List<OrderItem> items, BigDecimal totalAmount, OrderStatus status, Instant createdAt, Instant paidAt) {
        this.id = id; this.userId = userId; this.items = items; this.totalAmount = totalAmount; this.status = status; this.createdAt = createdAt; this.paidAt = paidAt;
    }

    /**
     * 复制当前订单并替换状态。
     *
     * @param nextStatus 新订单状态
     * @return 新订单视图
     */
    OrderView withStatus(OrderStatus nextStatus) { return new OrderView(id, userId, items, totalAmount, nextStatus, createdAt, nextStatus == OrderStatus.PAID ? Instant.now() : paidAt); }

    /** @return 订单 ID */
    public Long getId() { return id; }
    /** @return 下单用户 ID */
    public Long getUserId() { return userId; }
    /** @return 订单商品明细列表 */
    public List<OrderItem> getItems() { return items; }
    /** @return 订单总金额 */
    public BigDecimal getTotalAmount() { return totalAmount; }
    /** @return 订单当前状态 */
    public OrderStatus getStatus() { return status; }
    /** @return 订单创建时间 */
    public Instant getCreatedAt() { return createdAt; }
    /** @return 订单支付时间 */
    public Instant getPaidAt() { return paidAt; }
}
