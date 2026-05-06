package com.paymentsystem.common.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {
    private BigDecimal amount;

    public Money(BigDecimal amount) {
        BigDecimal normalized = amount.setScale(2, RoundingMode.HALF_UP);
        if (normalized.signum() < 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        this.amount = normalized;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
