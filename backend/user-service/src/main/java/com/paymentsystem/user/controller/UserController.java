package com.paymentsystem.user.controller;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.user.dto.AddressView;
import com.paymentsystem.common.security.CurrentUser;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户接口控制器，提供当前用户资料和收货地址查询能力。
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    /**
     * 查询当前登录用户资料。
     *
     * @param request HTTP 请求对象
     * @return 当前用户资料
     */
    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> profile(HttpServletRequest request) {
        CurrentUser current = UserContext.from(request);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userId", current.getUserId());
        data.put("username", current.getUsername());
        data.put("role", current.getRole());
        return ApiResponse.ok(data);
    }

    /**
     * 查询当前用户收货地址列表。
     *
     * @return 收货地址列表
     */
    @GetMapping("/addresses")
    public ApiResponse<List<AddressView>> addresses() {
        return ApiResponse.ok(Arrays.asList(new AddressView(1L, "张三", "13800000000", "上海市浦东新区示例路 1 号", true)));
    }
}
