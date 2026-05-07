package com.paymentsystem.auth.repository;

import com.paymentsystem.common.UserRole;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserAccountRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 创建账号仓储并注入 JDBC 访问入口。
     *
     * @param jdbcTemplate JDBC 操作模板
     */
    public UserAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 按用户名查询启用状态的账号。
     *
     * @param username 用户名
     * @return 账号信息，不存在时返回 null
     */
    public UserAccount findByUsername(String username) {
        return jdbcTemplate.query(
                "select id, username, password_hash, role from user_account where username = ? and status = 'ACTIVE'",
                rs -> rs.next()
                        ? new UserAccount(
                                rs.getLong("id"),
                                rs.getString("username"),
                                rs.getString("password_hash"),
                                UserRole.valueOf(rs.getString("role")))
                        : null,
                username);
    }

    /**
     * 创建新账号，用户名重复时返回 null。
     *
     * @param username 用户名
     * @param password 密码
     * @param role 用户角色
     * @return 创建后的账号
     */
    public UserAccount create(String username, String password, UserRole role) {
        try {
            jdbcTemplate.update(
                    "insert into user_account(username, password_hash, role, status) values (?, ?, ?, 'ACTIVE')",
                    username,
                    password,
                    role.name());
        } catch (DuplicateKeyException ex) {
            return null;
        }
        return findByUsername(username);
    }
}
