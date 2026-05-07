package com.paymentsystem.order;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.OrderStatus;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 订单接口控制器，负责创建订单、查询订单和推进演示订单状态。
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    /** 演示订单号自增生成器。 */
    private final AtomicLong ids = new AtomicLong(20260000);
    /** 以内存 Map 模拟订单存储。 */
    private final Map<Long, OrderView> orders = new ConcurrentHashMap<Long, OrderView>();

    /**
     * 创建当前用户的订单，并根据订单明细汇总订单金额。
     *
     * @param request HTTP 请求对象
     * @param command 创建订单请求
     * @return 创建后的订单视图
     */
    @PostMapping
    public ApiResponse<OrderView> create(HttpServletRequest request, @RequestBody CreateOrderRequest command) {
        Long userId = UserContext.from(request).getUserId();
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : safeItems(command.getItems())) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        OrderView order = new OrderView(ids.incrementAndGet(), userId, safeItems(command.getItems()), total, OrderStatus.CREATED, Instant.now(), null);
        orders.put(order.getId(), order);
        return ApiResponse.ok(order);
    }

    /**
     * 查询当前用户的订单列表。
     *
     * @param request HTTP 请求对象
     * @return 当前用户订单列表
     */
    @GetMapping
    public ApiResponse<List<OrderView>> list(HttpServletRequest request) {
        Long userId = UserContext.from(request).getUserId();
        List<OrderView> result = new ArrayList<OrderView>();
        for (OrderView order : orders.values()) {
            if (order.getUserId().equals(userId)) {
                result.add(order);
            }
        }
        return ApiResponse.ok(result);
    }

    /**
     * 查询指定订单详情。
     *
     * @param id 订单 ID
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public ApiResponse<OrderView> detail(@PathVariable Long id) {
        OrderView order = orders.get(id);
        return order == null ? ApiResponse.fail("ORDER_NOT_FOUND", "order not found") : ApiResponse.ok(order);
    }

    /**
     * 取消指定订单。
     *
     * @param id 订单 ID
     * @return 取消后的订单视图
     */
    @PostMapping("/{id}/cancel")
    public ApiResponse<OrderView> cancel(@PathVariable Long id) {
        return transition(id, OrderStatus.CANCELLED);
    }

    /**
     * 将指定订单标记为已支付。
     *
     * @param id 订单 ID
     * @return 支付后的订单视图
     */
    @PostMapping("/{id}/paid")
    public ApiResponse<OrderView> paid(@PathVariable Long id) {
        return transition(id, OrderStatus.PAID);
    }

    /**
     * 按订单状态机执行状态流转。
     *
     * @param id 订单 ID
     * @param target 目标状态
     * @return 流转后的订单视图
     */
    private ApiResponse<OrderView> transition(Long id, OrderStatus target) {
        OrderView order = orders.get(id);
        if (order == null) {
            return ApiResponse.fail("ORDER_NOT_FOUND", "order not found");
        }
        if (!OrderStatusMachine.canTransit(order.getStatus(), target)) {
            return ApiResponse.fail("ORDER_STATUS_INVALID", "invalid order status transition");
        }
        OrderView next = order.withStatus(target);
        orders.put(id, next);
        return ApiResponse.ok(next);
    }

    /**
     * 将空订单明细规整为空列表，减少调用方判空。
     *
     * @param items 原始订单明细
     * @return 非空订单明细列表
     */
    private List<OrderItem> safeItems(List<OrderItem> items) {
        return items == null ? new ArrayList<OrderItem>() : items;
    }
}
