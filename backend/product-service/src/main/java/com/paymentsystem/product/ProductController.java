package com.paymentsystem.product;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final Map<Long, ProductView> products = new ConcurrentHashMap<Long, ProductView>();

    public ProductController() {
        products.put(101L, new ProductView(101L, 10001L, "机械键盘 Pro", "办公外设", new BigDecimal("399.00"), 300, true, "适合高频输入的热插拔键盘"));
        products.put(102L, new ProductView(102L, 10002L, "无线降噪耳机", "数码音频", new BigDecimal("699.00"), 160, true, "通勤和办公两用"));
        products.put(103L, new ProductView(103L, 10003L, "人体工学椅", "办公家具", new BigDecimal("1299.00"), 48, true, "长时间工作支撑"));
    }

    @GetMapping
    public ApiResponse<List<ProductView>> list(@RequestParam(required = false) String keyword) {
        String filter = keyword == null ? "" : keyword.trim();
        List<ProductView> result = new ArrayList<ProductView>();
        for (ProductView item : products.values()) {
            if (item.isOnSale() && (filter.isEmpty() || item.getName().contains(filter) || item.getCategory().contains(filter))) {
                result.add(item);
            }
        }
        return ApiResponse.ok(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductView> detail(@PathVariable Long id) {
        ProductView product = products.get(id);
        if (product == null || !product.isOnSale()) {
            return ApiResponse.fail("PRODUCT_NOT_FOUND", "product is not available");
        }
        return ApiResponse.ok(product);
    }

    public static class ProductView {
        private Long id;
        private Long skuId;
        private String name;
        private String category;
        private BigDecimal price;
        private Integer stock;
        private boolean onSale;
        private String description;
        public ProductView(Long id, Long skuId, String name, String category, BigDecimal price, Integer stock, boolean onSale, String description) {
            this.id = id; this.skuId = skuId; this.name = name; this.category = category; this.price = price; this.stock = stock; this.onSale = onSale; this.description = description;
        }
        public Long getId() { return id; }
        public Long getSkuId() { return skuId; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public BigDecimal getPrice() { return price; }
        public Integer getStock() { return stock; }
        public boolean isOnSale() { return onSale; }
        public String getDescription() { return description; }
    }
}
