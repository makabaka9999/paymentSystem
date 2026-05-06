package com.paymentsystem.seckill;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/seckill")
public class SeckillController {
    private final Map<Long, SeckillActivity> activities = new ConcurrentHashMap<Long, SeckillActivity>();
    private final Map<String, SeckillRecord> records = new ConcurrentHashMap<String, SeckillRecord>();
    private final Set<String> userLocks = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    private final Map<Long, Integer> redisLikeStock = new ConcurrentHashMap<Long, Integer>();

    public SeckillController() {
        activities.put(501L, new SeckillActivity(501L, 101L, 10001L, "机械键盘 Pro 秒杀", new BigDecimal("199.00"), 50, Instant.now().minusSeconds(3600), Instant.now().plusSeconds(7200), true));
        redisLikeStock.put(501L, 50);
    }

    @GetMapping("/activities")
    public ApiResponse<List<SeckillActivity>> activities() {
        return ApiResponse.ok(new ArrayList<SeckillActivity>(activities.values()));
    }

    @PostMapping("/activities/{activityId}/preheat")
    public ApiResponse<Map<String, Object>> preheat(@PathVariable Long activityId) {
        SeckillActivity activity = activities.get(activityId);
        if (activity == null) {
            return ApiResponse.fail("ACTIVITY_NOT_FOUND", "activity not found");
        }
        redisLikeStock.put(activityId, activity.getStock());
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("activityId", activityId);
        data.put("preheatedStock", activity.getStock());
        return ApiResponse.ok(data);
    }

    @PostMapping("/activities/{activityId}/submit")
    public ApiResponse<SeckillRecord> submit(@PathVariable Long activityId, HttpServletRequest request) {
        Long userId = UserContext.from(request).getUserId();
        SeckillActivity activity = activities.get(activityId);
        if (activity == null || !activity.isActive(Instant.now())) {
            return ApiResponse.fail("ACTIVITY_CLOSED", "activity is not available");
        }
        String lock = activityId + ":" + userId;
        if (!userLocks.add(lock)) {
            return ApiResponse.fail("DUPLICATE_REQUEST", "user already joined this activity");
        }
        Integer stock = redisLikeStock.get(activityId);
        if (stock == null || stock <= 0) {
            userLocks.remove(lock);
            return ApiResponse.fail("SOLD_OUT", "seckill stock sold out");
        }
        redisLikeStock.put(activityId, stock - 1);
        String requestId = UUID.randomUUID().toString();
        SeckillRecord record = new SeckillRecord(requestId, activityId, userId, activity.getSkuId(), "QUEUED", "request accepted", Instant.now());
        records.put(requestId, record);
        return ApiResponse.accepted(record);
    }

    @GetMapping("/records/{requestId}")
    public ApiResponse<SeckillRecord> record(@PathVariable String requestId) {
        SeckillRecord record = records.get(requestId);
        return record == null ? ApiResponse.fail("RECORD_NOT_FOUND", "record not found") : ApiResponse.ok(record);
    }

    public static class SeckillActivity {
        private Long id;
        private Long productId;
        private Long skuId;
        private String title;
        private BigDecimal seckillPrice;
        private Integer stock;
        private Instant startsAt;
        private Instant endsAt;
        private boolean enabled;
        public SeckillActivity(Long id, Long productId, Long skuId, String title, BigDecimal seckillPrice, Integer stock, Instant startsAt, Instant endsAt, boolean enabled) {
            this.id = id; this.productId = productId; this.skuId = skuId; this.title = title; this.seckillPrice = seckillPrice; this.stock = stock; this.startsAt = startsAt; this.endsAt = endsAt; this.enabled = enabled;
        }
        boolean isActive(Instant now) { return enabled && !now.isBefore(startsAt) && now.isBefore(endsAt); }
        public Long getId() { return id; }
        public Long getProductId() { return productId; }
        public Long getSkuId() { return skuId; }
        public String getTitle() { return title; }
        public BigDecimal getSeckillPrice() { return seckillPrice; }
        public Integer getStock() { return stock; }
        public Instant getStartsAt() { return startsAt; }
        public Instant getEndsAt() { return endsAt; }
        public boolean isEnabled() { return enabled; }
    }
    public static class SeckillRecord {
        private String requestId;
        private Long activityId;
        private Long userId;
        private Long skuId;
        private String status;
        private String message;
        private Instant createdAt;
        public SeckillRecord(String requestId, Long activityId, Long userId, Long skuId, String status, String message, Instant createdAt) {
            this.requestId = requestId; this.activityId = activityId; this.userId = userId; this.skuId = skuId; this.status = status; this.message = message; this.createdAt = createdAt;
        }
        public String getRequestId() { return requestId; }
        public Long getActivityId() { return activityId; }
        public Long getUserId() { return userId; }
        public Long getSkuId() { return skuId; }
        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public Instant getCreatedAt() { return createdAt; }
    }
}
