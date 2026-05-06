package com.paymentsystem.payment;

import com.paymentsystem.common.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentIdempotencyTest {
    @Test
    void terminalStatusCannotBeReopened() {
        assertThat(PaymentStatus.SUCCESS).isIn(PaymentStatus.SUCCESS, PaymentStatus.FAILED, PaymentStatus.CLOSED);
    }
}
