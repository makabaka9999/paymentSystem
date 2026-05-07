package com.paymentsystem.seckill;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 秒杀接口控制器，提供活动查询、库存预热、请求提交和记录查询能力。
 */
@RestController
@RequestMapping("/api/seckill")
public class SeckillController {
    /** 以内存 Map 模拟秒杀活动存储。 */
    private final Map<Long, SeckillActivity> activities = new ConcurrentHashMap<Long, SeckillActivity>();
    /** 以内存 Map 模拟秒杀请求记录。 */
    private final Map<String, SeckillRecord> records = new ConcurrentHashMap<String, SeckillRecord>();
    /** 用户参与锁，防止同一用户重复提交同一活动。 */
    private final Set<String> userLocks = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    /** 模拟 Redis 中的活动库存。 */
    private final Map<Long, Integer> redisLikeStock = new ConcurrentHashMap<Long, Integer>();

    /**
     * 初始化演示秒杀活动和预热库存。
     */
    public SeckillController() {
        activities.put(501L, new SeckillActivity(501L, 101L, 10001L, "机械键盘 Pro 秒杀", new BigDecimal("199.00"), 50, Instant.now().minusSeconds(3600), Instant.now().plusSeconds(7200), true));
        redisLikeStock.put(501L, 50);
    }

    /**
     * 查询秒杀活动列表。
     *
     * @return 秒杀活动列表
     */
    @GetMapping("/activities")
    public ApiResponse<List<SeckillActivity>> activities() {
        return ApiResponse.ok(new ArrayList<SeckillActivity>(activities.values()));
    }

    /**
     * 将活动库存预热到模拟 Redis 库存中。
     *
     * @param activityId 活动 ID
     * @return 预热结果
     */
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

    /**
     * 提交秒杀请求，进行活动有效性、重复提交和库存校验。
     *
     * @param activityId 活动 ID
     * @param request HTTP 请求对象
     * @return 秒杀请求记录
     */
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

    /**
     * 查询秒杀请求记录。
     *
     * @param requestId 秒杀请求 ID
     * @return 秒杀请求记录
     */
    @GetMapping("/records/{requestId}")
    public ApiResponse<SeckillRecord> record(@PathVariable String requestId) {
        SeckillRecord record = records.get(requestId);
        return record == null ? ApiResponse.fail("RECORD_NOT_FOUND", "record not found") : ApiResponse.ok(record);
    }
}
