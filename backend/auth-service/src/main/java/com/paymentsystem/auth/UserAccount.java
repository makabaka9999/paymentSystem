package com.paymentsystem.auth;

import com.paymentsystem.common.UserRole;

/**
 * 演示环境中的用户账号实体。
 */
class UserAccount {
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
    UserAccount(Long id, String username, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /** @return 用户 ID */
    Long getId() { return id; }

    /** @return 用户名 */
    String getUsername() { return username; }

    /** @return 用户密码 */
    String getPassword() { return password; }

    /** @return 用户角色 */
    UserRole getRole() { return role; }
}
