package com.paymentsystem.cart.repository;

import com.paymentsystem.cart.dto.CartItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 创建购物车仓储并注入 JDBC 访问入口。
     *
     * @param jdbcTemplate JDBC 操作模板
     */
    public CartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询指定用户的购物车明细。
     *
     * @param userId 用户 ID
     * @return 购物车明细列表
     */
    public List<CartItem> findByUserId(Long userId) {
        return jdbcTemplate.query(
                "select sku_id, product_name, price, quantity from cart_item where user_id = ? order by id",
                (rs, rowNum) -> new CartItem(
                        rs.getLong("sku_id"),
                        rs.getString("product_name"),
                        rs.getBigDecimal("price"),
                        rs.getInt("quantity")),
                userId);
    }

    /**
     * 添加商品到购物车，已存在同 SKU 时累加数量。
     *
     * @param userId 用户 ID
     * @param item 购物车商品
     */
    public void add(Long userId, CartItem item) {
        jdbcTemplate.update(
                "insert into cart_item(user_id, sku_id, product_name, price, quantity) values (?, ?, ?, ?, ?) "
                        + "on duplicate key update product_name = values(product_name), price = values(price), quantity = quantity + values(quantity)",
                userId,
                item.getSkuId(),
                item.getProductName(),
                item.getPrice(),
                item.getQuantity());
    }

    /**
     * 更新购物车中指定 SKU 的购买数量。
     *
     * @param userId 用户 ID
     * @param skuId SKU ID
     * @param quantity 新数量
     */
    public void updateQuantity(Long userId, Long skuId, Integer quantity) {
        jdbcTemplate.update(
                "update cart_item set quantity = ? where user_id = ? and sku_id = ?",
                Math.max(1, quantity == null ? 1 : quantity),
                userId,
                skuId);
    }

    /**
     * 删除购物车中指定 SKU。
     *
     * @param userId 用户 ID
     * @param skuId SKU ID
     */
    public void delete(Long userId, Long skuId) {
        jdbcTemplate.update("delete from cart_item where user_id = ? and sku_id = ?", userId, skuId);
    }
}
