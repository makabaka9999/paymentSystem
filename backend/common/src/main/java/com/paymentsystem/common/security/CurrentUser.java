package com.paymentsystem.common.security;

import com.paymentsystem.common.UserRole;

public class CurrentUser {
    private Long userId;
    private String username;
    private UserRole role;

    public CurrentUser(Long userId, String username, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public UserRole getRole() {
        return role;
    }
}
