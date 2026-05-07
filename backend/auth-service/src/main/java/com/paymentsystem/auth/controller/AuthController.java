package com.paymentsystem.auth.controller;

import com.paymentsystem.auth.dto.AuthSession;
import com.paymentsystem.auth.dto.LoginRequest;
import com.paymentsystem.auth.dto.RegisterRequest;
import com.paymentsystem.auth.repository.UserAccount;
import com.paymentsystem.auth.repository.UserAccountRepository;
import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.UserRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserAccountRepository userAccountRepository;

    /**
     * 创建认证控制器并注入账号仓储。
     *
     * @param userAccountRepository 账号数据访问对象
     */
    public AuthController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    /**
     * 注册新账号，并在成功后返回登录会话。
     *
     * @param request 注册请求参数
     * @return 登录会话或用户名重复错误
     */
    @PostMapping("/register")
    public ApiResponse<AuthSession> register(@RequestBody RegisterRequest request) {
        UserRole role = request.getRole() == null ? UserRole.USER : request.getRole();
        UserAccount created = userAccountRepository.create(request.getUsername(), request.getPassword(), role);
        if (created == null) {
            return ApiResponse.fail("USER_EXISTS", "username already exists");
        }
        return ApiResponse.ok(session(created));
    }

    /**
     * 使用用户名和密码登录系统。
     *
     * @param request 登录请求参数
     * @return 登录会话或凭据错误
     */
    @PostMapping("/login")
    public ApiResponse<AuthSession> login(@RequestBody LoginRequest request) {
        UserAccount account = userAccountRepository.findByUsername(request.getUsername());
        if (account == null || !account.getPassword().equals(request.getPassword())) {
            return ApiResponse.fail("BAD_CREDENTIALS", "invalid username or password");
        }
        return ApiResponse.ok(session(account));
    }

    /**
     * 查询当前请求携带的用户信息。
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
     * @param account 已认证账号
     * @return 登录会话
     */
    private AuthSession session(UserAccount account) {
        String token = "demo." + account.getId() + "." + account.getRole() + "." + Instant.now().toEpochMilli();
        return new AuthSession(token, account.getId(), account.getUsername(), account.getRole());
    }
}
