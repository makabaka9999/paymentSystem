package com.paymentsystem.order;

import com.paymentsystem.common.OrderStatus;
import com.paymentsystem.order.domain.OrderStatusMachine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusMachineTest {
    @Test
    void paidOrderCanShipButCannotClose() {
        assertThat(OrderStatusMachine.canTransit(OrderStatus.PAID, OrderStatus.SHIPPED)).isTrue();
        assertThat(OrderStatusMachine.canTransit(OrderStatus.PAID, OrderStatus.CLOSED)).isFalse();
    }
}
