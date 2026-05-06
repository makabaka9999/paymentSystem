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

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AtomicLong ids = new AtomicLong(1000);
    private final Map<String, UserAccount> users = new ConcurrentHashMap<String, UserAccount>();

    public AuthController() {
        users.put("user", new UserAccount(1L, "user", "password", UserRole.USER));
        users.put("merchant", new UserAccount(2L, "merchant", "password", UserRole.MERCHANT));
        users.put("admin", new UserAccount(3L, "admin", "password", UserRole.ADMIN));
    }

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

    @PostMapping("/login")
    public ApiResponse<AuthSession> login(@RequestBody LoginRequest request) {
        UserAccount account = users.get(request.getUsername());
        if (account == null || !account.getPassword().equals(request.getPassword())) {
            return ApiResponse.fail("BAD_CREDENTIALS", "invalid username or password");
        }
        return ApiResponse.ok(session(account));
    }

    @GetMapping("/me")
    public ApiResponse<Map<String, Object>> me(HttpServletRequest request) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userId", request.getHeader("X-User-Id") == null ? "1" : request.getHeader("X-User-Id"));
        data.put("username", request.getHeader("X-User-Name") == null ? "user" : request.getHeader("X-User-Name"));
        data.put("role", request.getHeader("X-User-Role") == null ? "USER" : request.getHeader("X-User-Role"));
        return ApiResponse.ok(data);
    }

    private AuthSession session(UserAccount account) {
        String token = "demo." + account.getId() + "." + account.getRole() + "." + Instant.now().toEpochMilli();
        return new AuthSession(token, account.getId(), account.getUsername(), account.getRole());
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private UserRole role;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public UserRole getRole() { return role; }
        public void setRole(UserRole role) { this.role = role; }
    }

    public static class LoginRequest {
        private String username;
        private String password;
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class AuthSession {
        private String accessToken;
        private Long userId;
        private String username;
        private UserRole role;
        public AuthSession(String accessToken, Long userId, String username, UserRole role) {
            this.accessToken = accessToken;
            this.userId = userId;
            this.username = username;
            this.role = role;
        }
        public String getAccessToken() { return accessToken; }
        public Long getUserId() { return userId; }
        public String getUsername() { return username; }
        public UserRole getRole() { return role; }
    }

    private static class UserAccount {
        private Long id;
        private String username;
        private String password;
        private UserRole role;
        UserAccount(Long id, String username, String password, UserRole role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
        }
        Long getId() { return id; }
        String getUsername() { return username; }
        String getPassword() { return password; }
        UserRole getRole() { return role; }
    }
}
