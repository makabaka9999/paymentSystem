package com.paymentsystem.admin;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @GetMapping("/users")
    public ApiResponse<List<Map<String, Object>>> users() {
        Map<String, Object> user = new HashMap<String, Object>();
        user.put("id", 1);
        user.put("username", "user");
        user.put("role", "USER");
        Map<String, Object> merchant = new HashMap<String, Object>();
        merchant.put("id", 2);
        merchant.put("username", "merchant");
        merchant.put("role", "MERCHANT");
        return ApiResponse.ok(Arrays.asList(user, merchant));
    }

    @GetMapping("/merchants")
    public ApiResponse<List<Map<String, Object>>> merchants() {
        Map<String, Object> merchant = new HashMap<String, Object>();
        merchant.put("id", 10);
        merchant.put("name", "示例商户");
        merchant.put("status", "APPROVED");
        return ApiResponse.ok(Arrays.asList(merchant));
    }

    @PostMapping("/products/{id}/approve")
    public ApiResponse<Map<String, Object>> approveProduct(@PathVariable Long id) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productId", id);
        data.put("auditStatus", "APPROVED");
        return ApiResponse.ok(data);
    }

    @PostMapping("/seckill/activities")
    public ApiResponse<Map<String, Object>> createSeckill(@RequestBody CreateSeckillActivity request) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("activityId", 501);
        data.put("title", request.getTitle());
        data.put("status", "CREATED");
        data.put("createdAt", Instant.now());
        return ApiResponse.ok(data);
    }

    public static class CreateSeckillActivity {
        private Long productId;
        private Long skuId;
        private String title;
        private BigDecimal seckillPrice;
        private Integer stock;
        private Instant startsAt;
        private Instant endsAt;
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Long getSkuId() { return skuId; }
        public void setSkuId(Long skuId) { this.skuId = skuId; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public BigDecimal getSeckillPrice() { return seckillPrice; }
        public void setSeckillPrice(BigDecimal seckillPrice) { this.seckillPrice = seckillPrice; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
        public Instant getStartsAt() { return startsAt; }
        public void setStartsAt(Instant startsAt) { this.startsAt = startsAt; }
        public Instant getEndsAt() { return endsAt; }
        public void setEndsAt(Instant endsAt) { this.endsAt = endsAt; }
    }
}
