package com.paymentsystem.auth;

import com.paymentsystem.common.UserRole;

/**
 * 登录成功后的会话响应。
 */
public class AuthSession {
    /** 访问令牌，演示项目中为可读的模拟 token。 */
    private String accessToken;
    /** 当前登录用户 ID。 */
    private Long userId;
    /** 当前登录用户名。 */
    private String username;
    /** 当前登录用户角色。 */
    private UserRole role;

    /**
     * 创建会话响应。
     *
     * @param accessToken 访问令牌
     * @param userId 用户 ID
     * @param username 用户名
     * @param role 用户角色
     */
    public AuthSession(String accessToken, Long userId, String username, UserRole role) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    /**
     * 获取访问令牌。
     *
     * @return 访问令牌
     */
    public String getAccessToken() { return accessToken; }

    /**
     * 获取用户 ID。
     *
     * @return 用户 ID
     */
    public Long getUserId() { return userId; }

    /**
     * 获取用户名。
     *
     * @return 用户名
     */
    public String getUsername() { return username; }

    /**
     * 获取用户角色。
     *
     * @return 用户角色
     */
    public UserRole getRole() { return role; }
}
