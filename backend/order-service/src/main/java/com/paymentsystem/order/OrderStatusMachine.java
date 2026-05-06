package com.paymentsystem.order;

import com.paymentsystem.common.OrderStatus;

import java.util.*;

public final class OrderStatusMachine {
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new HashMap<OrderStatus, Set<OrderStatus>>();

    static {
        ALLOWED.put(OrderStatus.CREATED, EnumSet.of(OrderStatus.PAYING, OrderStatus.CANCELLED, OrderStatus.CLOSED));
        ALLOWED.put(OrderStatus.PAYING, EnumSet.of(OrderStatus.PAID, OrderStatus.CANCELLED, OrderStatus.CLOSED));
        ALLOWED.put(OrderStatus.PAID, EnumSet.of(OrderStatus.SHIPPED, OrderStatus.REFUNDED));
        ALLOWED.put(OrderStatus.SHIPPED, EnumSet.of(OrderStatus.FINISHED));
        ALLOWED.put(OrderStatus.FINISHED, EnumSet.of(OrderStatus.REFUNDED));
    }

    private OrderStatusMachine() {
    }

    public static boolean canTransit(OrderStatus from, OrderStatus to) {
        Set<OrderStatus> targets = ALLOWED.get(from);
        return targets != null && targets.contains(to);
    }
}
