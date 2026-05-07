package com.paymentsystem.common.web;

import com.paymentsystem.common.UserRole;
import com.paymentsystem.common.security.CurrentUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 从请求头解析当前用户上下文的工具类。
 */
public final class UserContext {
    /**
     * 禁止实例化工具类。
     */
    private UserContext() {
    }

    /**
     * 从网关注入的请求头解析当前用户，缺省时返回演示用户。
     *
     * @param request HTTP 请求对象
     * @return 当前用户上下文
     */
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
