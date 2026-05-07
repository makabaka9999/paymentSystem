package com.paymentsystem.common.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 金额值对象，统一金额精度和非负校验。
 */
public class Money {
    /** 金额数值，固定保留两位小数。 */
    private BigDecimal amount;

    /**
     * 创建金额值对象。
     *
     * @param amount 原始金额
     */
    public Money(BigDecimal amount) {
        BigDecimal normalized = amount.setScale(2, RoundingMode.HALF_UP);
        if (normalized.signum() < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.amount = normalized;
    }

    /** @return 固定两位小数的金额 */
    public BigDecimal getAmount() {
        return amount;
    }
}
