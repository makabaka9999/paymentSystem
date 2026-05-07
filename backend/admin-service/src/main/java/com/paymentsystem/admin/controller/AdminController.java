package com.paymentsystem.admin.controller;

import com.paymentsystem.admin.dto.CreateSeckillActivity;
import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理后台接口控制器，提供用户、商户、商品审核和秒杀活动管理能力。
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    /**
     * 查询平台用户概览。
     *
     * @return 用户列表
     */
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

    /**
     * 查询商户审核概览。
     *
     * @return 商户列表
     */
    @GetMapping("/merchants")
    public ApiResponse<List<Map<String, Object>>> merchants() {
        Map<String, Object> merchant = new HashMap<String, Object>();
        merchant.put("id", 10);
        merchant.put("name", "示例商户");
        merchant.put("status", "APPROVED");
        return ApiResponse.ok(Arrays.asList(merchant));
    }

    /**
     * 审核通过指定商品。
     *
     * @param id 商品 ID
     * @return 审核结果
     */
    @PostMapping("/products/{id}/approve")
    public ApiResponse<Map<String, Object>> approveProduct(@PathVariable Long id) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("productId", id);
        data.put("auditStatus", "APPROVED");
        return ApiResponse.ok(data);
    }

    /**
     * 创建秒杀活动。
     *
     * @param request 秒杀活动创建参数
     * @return 创建结果
     */
    @PostMapping("/seckill/activities")
    public ApiResponse<Map<String, Object>> createSeckill(@RequestBody CreateSeckillActivity request) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("activityId", 501);
        data.put("title", request.getTitle());
        data.put("status", "CREATED");
        data.put("createdAt", Instant.now());
        return ApiResponse.ok(data);
    }
}
