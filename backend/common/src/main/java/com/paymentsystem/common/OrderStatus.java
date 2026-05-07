package com.paymentsystem.common;

/**
 * 订单状态枚举。
 */
public enum OrderStatus {
    /** 订单已创建。 */
    CREATED,
    /** 订单支付中。 */
    PAYING,
    /** 订单已支付。 */
    PAID,
    /** 订单已取消。 */
    CANCELLED,
    /** 订单已关闭。 */
    CLOSED,
    /** 订单已发货。 */
    SHIPPED,
    /** 订单已完成。 */
    FINISHED,
    /** 订单已退款。 */
    REFUNDED
}
