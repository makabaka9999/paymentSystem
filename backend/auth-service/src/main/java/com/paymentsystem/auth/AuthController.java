package com.paymentsystem.auth;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.UserRole;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 认证接口控制器，提供演示账号注册、登录和当前用户信息查询能力。
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /** 演示用户自增主键生成器。 */
    private final AtomicLong ids = new AtomicLong(1000);
    /** 以内存 Map 模拟用户账号存储。 */
    private final Map<String, UserAccount> users = new ConcurrentHashMap<String, UserAccount>();

    /**
     * 初始化演示账号，便于前端和接口联调。
     */
    public AuthController() {
        users.put("user", new UserAccount(1L, "user", "password", UserRole.USER));
        users.put("merchant", new UserAccount(2L, "merchant", "password", UserRole.MERCHANT));
        users.put("admin", new UserAccount(3L, "admin", "password", UserRole.ADMIN));
    }

    /**
     * 注册新用户，用户名重复时返回业务失败。
     *
     * @param request 注册请求参数
     * @return 登录会话信息
     */
    @PostMapping("/register")
    public ApiResponse<AuthSession> register(@RequestBody RegisterRequest request) {
        UserRole role = request.getRole() == null ? UserRole.USER : request.getRole();
        UserAccount created = new UserAccount(ids.incrementAndGet(), request.getUsername(), request.getPassword(), role);
        UserAccount previous = users.putIfAbsent(request.getUsername(), created);
        if (previous != null) {
            return ApiResponse.fail("USER_EXISTS", "username already exists");
        }
        return ApiResponse.ok(session(created));
    }

    /**
     * 使用用户名和密码登录演示系统。
     *
     * @param request 登录请求参数
     * @return 登录会话信息
     */
    @PostMapping("/login")
    public ApiResponse<AuthSession> login(@RequestBody LoginRequest request) {
        UserAccount account = users.get(request.getUsername());
        if (account == null || !account.getPassword().equals(request.getPassword())) {
            return ApiResponse.fail("BAD_CREDENTIALS", "invalid username or password");
        }
        return ApiResponse.ok(session(account));
    }

    /**
     * 查询当前请求中的用户信息，未携带网关注入头时返回默认演示用户。
     *
     * @param request HTTP 请求对象
     * @return 当前用户基础信息
     */
    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userId", request.getHeader("X-User-Id") == null ? "1" : request.getHeader("X-User-Id"));
        data.put("username", request.getHeader("X-User-Name") == null ? "user" : request.getHeader("X-User-Name"));
        data.put("role", request.getHeader("X-User-Role") == null ? "USER" : request.getHeader("X-User-Role"));
        return ApiResponse.ok(data);
    }

    /**
     * 根据账号生成演示访问令牌和会话响应。
     *
     * @param account 用户账号
     * @return 会话响应对象
     */
    private AuthSession session(UserAccount account) {
        String token = "demo." + account.getId() + "." + account.getRole() + "." + Instant.now().toEpochMilli();
        return new AuthSession(token, account.getId(), account.getUsername(), account.getRole());
    }
}
