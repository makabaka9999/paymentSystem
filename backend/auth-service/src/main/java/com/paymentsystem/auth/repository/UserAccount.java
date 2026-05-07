package com.paymentsystem.auth.repository;

import com.paymentsystem.common.UserRole;

/**
 * 演示环境中的用户账号实体。
 */
public class UserAccount {
    /** 用户 ID。 */
    private Long id;
    /** 用户名。 */
    private String username;
    /** 用户密码。 */
    private String password;
    /** 用户角色。 */
    private UserRole role;

    /**
     * 创建用户账号。
     *
     * @param id 用户 ID
     * @param username 用户名
     * @param password 用户密码
     * @param role 用户角色
     */
    public UserAccount(Long id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /** @return 用户 ID */
    public Long getId() { return id; }

    /** @return 用户名 */
    public String getUsername() { return username; }

    /** @return 用户密码 */
    public String getPassword() { return password; }

    /** @return 用户角色 */
    public UserRole getRole() { return role; }
}
