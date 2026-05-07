package com.paymentsystem.merchant;

import java.math.BigDecimal;

/**
 * 商户商品保存请求参数。
 */
public class ProductCommand {
    /** 商品名称。 */
    private String name;
    /** 商品类目。 */
    private String category;
    /** 商品售价。 */
    private BigDecimal price;
    /** 商品库存数量。 */
    private Integer stock;
    /** 商品描述。 */
    private String description;

    /**
     * 转换为商户商品实体。
     *
     * @param id 商品 ID
     * @param onSale 是否上架
     * @return 商户商品实体
     */
    MerchantProduct toProduct(Long id, boolean onSale) { return new MerchantProduct(id, name, category, price, stock, description, onSale, "PENDING_APPROVAL"); }

    /** @return 商品名称 */
    public String getName() { return name; }
    /** @param name 商品名称 */
    public void setName(String name) { this.name = name; }
    /** @return 商品类目 */
    public String getCategory() { return category; }
    /** @param category 商品类目 */
    public void setCategory(String category) { this.category = category; }
    /** @return 商品售价 */
    public BigDecimal getPrice() { return price; }
    /** @param price 商品售价 */
    public void setPrice(BigDecimal price) { this.price = price; }
    /** @return 商品库存数量 */
    public Integer getStock() { return stock; }
    /** @param stock 商品库存数量 */
    public void setStock(Integer stock) { this.stock = stock; }
    /** @return 商品描述 */
    public String getDescription() { return description; }
    /** @param description 商品描述 */
    public void setDescription(String description) { this.description = description; }
}
