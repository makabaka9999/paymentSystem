package com.paymentsystem.merchant;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/merchant")
public class MerchantProductController {
    private final AtomicLong ids = new AtomicLong(300);
    private final Map<Long, MerchantProduct> products = new ConcurrentHashMap<Long, MerchantProduct>();

    @PostMapping("/products")
    public ApiResponse<MerchantProduct> create(@RequestBody ProductCommand command) {
        Long id = ids.incrementAndGet();
        MerchantProduct product = command.toProduct(id, false);
        products.put(id, product);
        return ApiResponse.ok(product);
    }

    @PutMapping("/products/{id}")
    public ApiResponse<MerchantProduct> update(@PathVariable Long id, @RequestBody ProductCommand command) {
        MerchantProduct current = products.get(id);
        MerchantProduct product = command.toProduct(id, current != null && current.isOnSale());
        products.put(id, product);
        return ApiResponse.ok(product);
    }

    @PutMapping("/products/{id}/on-sale")
    public ApiResponse<MerchantProduct> onSale(@PathVariable Long id) {
        return changeSaleState(id, true);
    }

    @PutMapping("/products/{id}/off-sale")
    public ApiResponse<MerchantProduct> offSale(@PathVariable Long id) {
        return changeSaleState(id, false);
    }

    @GetMapping("/orders")
    public ApiResponse<List<Map<String, Object>>> orders() {
        Map<String, Object> order = new HashMap<String, Object>();
        order.put("orderId", 20260001);
        order.put("status", "PAID");
        order.put("amount", "399.00");
        return ApiResponse.ok(Arrays.asList(order));
    }

    private ApiResponse<MerchantProduct> changeSaleState(Long id, boolean onSale) {
        MerchantProduct current = products.get(id);
        if (current == null) {
            return ApiResponse.fail("PRODUCT_NOT_FOUND", "merchant product not found");
        }
        MerchantProduct next = current.withSaleState(onSale);
        products.put(id, next);
        return ApiResponse.ok(next);
    }

    public static class ProductCommand {
        private String name;
        private String category;
        private BigDecimal price;
        private Integer stock;
        private String description;
        MerchantProduct toProduct(Long id, boolean onSale) { return new MerchantProduct(id, name, category, price, stock, description, onSale, "PENDING_APPROVAL"); }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
    public static class MerchantProduct {
        private Long id;
        private String name;
        private String category;
        private BigDecimal price;
        private Integer stock;
        private String description;
        private boolean onSale;
        private String auditStatus;
        public MerchantProduct(Long id, String name, String category, BigDecimal price, Integer stock, String description, boolean onSale, String auditStatus) {
            this.id = id; this.name = name; this.category = category; this.price = price; this.stock = stock; this.description = description; this.onSale = onSale; this.auditStatus = auditStatus;
        }
        MerchantProduct withSaleState(boolean next) { return new MerchantProduct(id, name, category, price, stock, description, next, auditStatus); }
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public BigDecimal getPrice() { return price; }
        public Integer getStock() { return stock; }
        public String getDescription() { return description; }
        public boolean isOnSale() { return onSale; }
        public String getAuditStatus() { return auditStatus; }
    }
}
