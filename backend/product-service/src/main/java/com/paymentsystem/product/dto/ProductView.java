package com.paymentsystem.product.dto;

import java.math.BigDecimal;

/**
 * 商品响应视图。
 */
public class ProductView {
    /** 商品 ID。 */
    private Long id;
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 商品名称。 */
    private String name;
    /** 商品类目。 */
    private String category;
    /** 商品售价。 */
    private BigDecimal price;
    /** 商品库存数量。 */
    private Integer stock;
    /** 是否上架销售。 */
    private boolean onSale;
    /** 商品描述。 */
    private String description;

    /**
     * 创建商品响应视图。
     */
    public ProductView(Long id, Long skuId, String name, String category, BigDecimal price, Integer stock, boolean onSale, String description) {
        this.id = id; this.skuId = skuId; this.name = name; this.category = category; this.price = price; this.stock = stock; this.onSale = onSale; this.description = description;
    }

    /** @return 商品 ID */
    public Long getId() { return id; }
    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @return 商品名称 */
    public String getName() { return name; }
    /** @return 商品类目 */
    public String getCategory() { return category; }
    /** @return 商品售价 */
    public BigDecimal getPrice() { return price; }
    /** @return 商品库存数量 */
    public Integer getStock() { return stock; }
    /** @return 是否上架销售 */
    public boolean isOnSale() { return onSale; }
    /** @return 商品描述 */
    public String getDescription() { return description; }
}
