package com.paymentsystem.common.security;

import com.paymentsystem.common.UserRole;

/**
 * 当前请求用户上下文。
 */
public class CurrentUser {
    /** 用户 ID。 */
    private Long userId;
    /** 用户名。 */
    private String username;
    /** 用户角色。 */
    private UserRole role;

    /**
     * 创建当前用户上下文。
     */
    public CurrentUser(Long userId, String username, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    /** @return 用户 ID */
    public Long getUserId() {
        return userId;
    }

    /** @return 用户名 */
    public String getUsername() {
        return username;
    }

    /** @return 用户角色 */
    public UserRole getRole() {
        return role;
    }
}
