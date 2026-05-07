package com.paymentsystem.order.domain;

import com.paymentsystem.common.OrderStatus;

import java.util.*;

/**
 * 订单状态机，集中维护订单状态允许流转关系。
 */
public final class OrderStatusMachine {
    /** 每个订单状态允许流转到的目标状态集合。 */
    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = new HashMap<OrderStatus, Set<OrderStatus>>();

    static {
        ALLOWED.put(OrderStatus.CREATED, EnumSet.of(OrderStatus.PAYING, OrderStatus.CANCELLED, OrderStatus.CLOSED));
        ALLOWED.put(OrderStatus.PAYING, EnumSet.of(OrderStatus.PAID, OrderStatus.CANCELLED, OrderStatus.CLOSED));
        ALLOWED.put(OrderStatus.PAID, EnumSet.of(OrderStatus.SHIPPED, OrderStatus.REFUNDED));
        ALLOWED.put(OrderStatus.SHIPPED, EnumSet.of(OrderStatus.FINISHED));
        ALLOWED.put(OrderStatus.FINISHED, EnumSet.of(OrderStatus.REFUNDED));
    }

    /**
     * 禁止实例化工具类。
     */
    private OrderStatusMachine() {
    }

    /**
     * 判断订单状态是否允许从当前状态流转到目标状态。
     *
     * @param from 当前状态
     * @param to 目标状态
     * @return 是否允许流转
     */
    public static boolean canTransit(OrderStatus from, OrderStatus to) {
        Set<OrderStatus> targets = ALLOWED.get(from);
        return targets != null && targets.contains(to);
    }
}
