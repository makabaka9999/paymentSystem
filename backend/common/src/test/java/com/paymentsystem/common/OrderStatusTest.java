package com.paymentsystem.common;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {
    @Test
    void containsAllMvpStates() {
        assertThat(OrderStatus.values()).contains(
                OrderStatus.CREATED,
                OrderStatus.PAYING,
                OrderStatus.PAID,
                OrderStatus.CANCELLED,
                OrderStatus.CLOSED,
                OrderStatus.SHIPPED,
                OrderStatus.FINISHED,
                OrderStatus.REFUNDED
        );
    }
}
