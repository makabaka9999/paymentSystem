package com.paymentsystem.common;

/**
 * 支付状态枚举。
 */
public enum PaymentStatus {
    /** 支付单已初始化。 */
    INIT,
    /** 支付处理中。 */
    PROCESSING,
    /** 支付成功。 */
    SUCCESS,
    /** 支付失败。 */
    FAILED,
    /** 支付单已关闭。 */
    CLOSED
}
