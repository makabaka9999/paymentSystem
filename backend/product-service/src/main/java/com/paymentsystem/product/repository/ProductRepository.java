package com.paymentsystem.product.repository;

import com.paymentsystem.product.dto.ProductView;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * 创建商品仓储并注入 JDBC 访问入口。
     *
     * @param jdbcTemplate JDBC 操作模板
     */
    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 查询上架商品列表，可按商品名称或类目过滤。
     *
     * @param keyword 搜索关键字
     * @return 上架商品列表
     */
    public List<ProductView> findOnSale(String keyword) {
        String filter = keyword == null ? "" : keyword.trim();
        String like = "%" + filter + "%";
        return jdbcTemplate.query(
                "select p.id, s.id as sku_id, p.name, p.category, s.price, coalesce(st.available, 0) as stock, p.on_sale, p.description "
                        + "from product p "
                        + "join product_sku s on s.product_id = p.id "
                        + "left join inventory_db.sku_stock st on st.sku_id = s.id "
                        + "where p.on_sale = 1 and (? = '' or p.name like ? or p.category like ?) "
                        + "order by p.id",
                (rs, rowNum) -> new ProductView(
                        rs.getLong("id"),
                        rs.getLong("sku_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock"),
                        rs.getBoolean("on_sale"),
                        rs.getString("description")),
                filter,
                like,
                like);
    }

    /**
     * 查询指定 ID 的上架商品详情。
     *
     * @param id 商品 ID
     * @return 商品详情，不存在时返回 null
     */
    public ProductView findOnSaleById(Long id) {
        List<ProductView> products = jdbcTemplate.query(
                "select p.id, s.id as sku_id, p.name, p.category, s.price, coalesce(st.available, 0) as stock, p.on_sale, p.description "
                        + "from product p "
                        + "join product_sku s on s.product_id = p.id "
                        + "left join inventory_db.sku_stock st on st.sku_id = s.id "
                        + "where p.id = ? and p.on_sale = 1",
                (rs, rowNum) -> new ProductView(
                        rs.getLong("id"),
                        rs.getLong("sku_id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getBigDecimal("price"),
                        rs.getInt("stock"),
                        rs.getBoolean("on_sale"),
                        rs.getString("description")),
                id);
        return products.isEmpty() ? null : products.get(0);
    }
}
