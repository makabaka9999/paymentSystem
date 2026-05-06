package com.paymentsystem.cart;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final Map<Long, Map<Long, CartItem>> carts = new ConcurrentHashMap<Long, Map<Long, CartItem>>();

    @GetMapping
    public ApiResponse<ArrayList<CartItem>> get(HttpServletRequest request) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.get(userId);
        return ApiResponse.ok(new ArrayList<CartItem>(userCart == null ? new ConcurrentHashMap<Long, CartItem>().values() : userCart.values()));
    }

    @PostMapping("/items")
    public ApiResponse<ArrayList<CartItem>> add(HttpServletRequest request, @RequestBody CartCommand command) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.computeIfAbsent(userId, ignored -> new ConcurrentHashMap<Long, CartItem>());
        CartItem existing = userCart.get(command.getSkuId());
        userCart.put(command.getSkuId(), existing == null ? command.toItem() : existing.withQuantity(existing.getQuantity() + command.getQuantity()));
        return get(request);
    }

    @PutMapping("/items/{skuId}")
    public ApiResponse<ArrayList<CartItem>> update(HttpServletRequest request, @PathVariable Long skuId, @RequestBody CartCommand command) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.computeIfAbsent(userId, ignored -> new ConcurrentHashMap<Long, CartItem>());
        CartItem existing = userCart.get(skuId);
        if (existing != null) {
            userCart.put(skuId, existing.withQuantity(command.getQuantity()));
        }
        return get(request);
    }

    @DeleteMapping("/items/{skuId}")
    public ApiResponse<ArrayList<CartItem>> delete(HttpServletRequest request, @PathVariable Long skuId) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.get(userId);
        if (userCart != null) {
            userCart.remove(skuId);
        }
        return get(request);
    }

    public static class CartItem {
        private Long skuId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        public CartItem(Long skuId, String productName, BigDecimal price, Integer quantity) { this.skuId = skuId; this.productName = productName; this.price = price; this.quantity = quantity; }
        CartItem withQuantity(Integer nextQuantity) { return new CartItem(skuId, productName, price, Math.max(1, nextQuantity)); }
        public Long getSkuId() { return skuId; }
        public String getProductName() { return productName; }
        public BigDecimal getPrice() { return price; }
        public Integer getQuantity() { return quantity; }
    }
    public static class CartCommand {
        private Long skuId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        CartItem toItem() { return new CartItem(skuId, productName, price, getQuantity()); }
        public Long getSkuId() { return skuId; }
        public void setSkuId(Long skuId) { this.skuId = skuId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Integer getQuantity() { return quantity == null ? 1 : quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
