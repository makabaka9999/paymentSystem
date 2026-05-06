package com.paymentsystem.common.web;

import com.paymentsystem.common.UserRole;
import com.paymentsystem.common.security.CurrentUser;

import javax.servlet.http.HttpServletRequest;

public final class UserContext {
    private UserContext() {
    }

    public static CurrentUser from(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        String name = request.getHeader("X-User-Name");
        String role = request.getHeader("X-User-Role");
        return new CurrentUser(
                id == null || id.trim().isEmpty() ? 1L : Long.parseLong(id),
                name == null || name.trim().isEmpty() ? "demo-user" : name,
                role == null || role.trim().isEmpty() ? UserRole.USER : UserRole.valueOf(role)
        );
    }
}
