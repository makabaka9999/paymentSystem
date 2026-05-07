package com.paymentsystem.auth;

import com.paymentsystem.common.UserRole;

/**
 * 用户注册请求参数。
 */
public class RegisterRequest {
    /** 用户名，作为演示账号的唯一标识。 */
    private String username;
    /** 登录密码，演示项目中以明文方式暂存。 */
    private String password;
    /** 用户角色，未传时默认注册为普通用户。 */
    private UserRole role;

    /**
     * 获取注册用户名。
     *
     * @return 用户名
     */
    public String getUsername() { return username; }

    /**
     * 设置注册用户名。
     *
     * @param username 用户名
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * 获取注册密码。
     *
     * @return 密码
     */
    public String getPassword() { return password; }

    /**
     * 设置注册密码。
     *
     * @param password 密码
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * 获取用户角色。
     *
     * @return 用户角色
     */
    public UserRole getRole() { return role; }

    /**
     * 设置用户角色。
     *
     * @param role 用户角色
     */
    public void setRole(UserRole role) { this.role = role; }
}
