package com.paymentsystem.auth;

/**
 * 用户登录请求参数。
 */
public class LoginRequest {
    /** 登录用户名。 */
    private String username;
    /** 登录密码。 */
    private String password;

    /**
     * 获取登录用户名。
     *
     * @return 用户名
     */
    public String getUsername() { return username; }

    /**
     * 设置登录用户名。
     *
     * @param username 用户名
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * 获取登录密码。
     *
     * @return 密码
     */
    public String getPassword() { return password; }

    /**
     * 设置登录密码。
     *
     * @param password 密码
     */
    public void setPassword(String password) { this.password = password; }
}
