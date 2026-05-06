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

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final AtomicLong ids = new AtomicLong(20260000);
    private final Map<Long, OrderView> orders = new ConcurrentHashMap<Long, OrderView>();

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

    @GetMapping("/{id}")
    public ApiResponse<OrderView> detail(@PathVariable Long id) {
        OrderView order = orders.get(id);
        return order == null ? ApiResponse.fail("ORDER_NOT_FOUND", "order not found") : ApiResponse.ok(order);
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<OrderView> cancel(@PathVariable Long id) {
        return transition(id, OrderStatus.CANCELLED);
    }

    @PostMapping("/{id}/paid")
    public ApiResponse<OrderView> paid(@PathVariable Long id) {
        return transition(id, OrderStatus.PAID);
    }

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

    private List<OrderItem> safeItems(List<OrderItem> items) {
        return items == null ? new ArrayList<OrderItem>() : items;
    }

    public static class CreateOrderRequest {
        private List<OrderItem> items;
        private String addressId;
        public List<OrderItem> getItems() { return items; }
        public void setItems(List<OrderItem> items) { this.items = items; }
        public String getAddressId() { return addressId; }
        public void setAddressId(String addressId) { this.addressId = addressId; }
    }
    public static class OrderItem {
        private Long skuId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        public Long getSkuId() { return skuId; }
        public void setSkuId(Long skuId) { this.skuId = skuId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public BigDecimal getPrice() { return price == null ? BigDecimal.ZERO : price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Integer getQuantity() { return quantity == null ? 1 : quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    public static class OrderView {
        private Long id;
        private Long userId;
        private List<OrderItem> items;
        private BigDecimal totalAmount;
        private OrderStatus status;
        private Instant createdAt;
        private Instant paidAt;
        public OrderView(Long id, Long userId, List<OrderItem> items, BigDecimal totalAmount, OrderStatus status, Instant createdAt, Instant paidAt) {
            this.id = id; this.userId = userId; this.items = items; this.totalAmount = totalAmount; this.status = status; this.createdAt = createdAt; this.paidAt = paidAt;
        }
        OrderView withStatus(OrderStatus nextStatus) { return new OrderView(id, userId, items, totalAmount, nextStatus, createdAt, nextStatus == OrderStatus.PAID ? Instant.now() : paidAt); }
        public Long getId() { return id; }
        public Long getUserId() { return userId; }
        public List<OrderItem> getItems() { return items; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public OrderStatus getStatus() { return status; }
        public Instant getCreatedAt() { return createdAt; }
        public Instant getPaidAt() { return paidAt; }
    }
}
