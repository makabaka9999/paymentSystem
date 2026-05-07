package com.paymentsystem.merchant;

import java.math.BigDecimal;

/**
 * 商户商品实体。
 */
public class MerchantProduct {
    /** 商品 ID。 */
    private Long id;
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
    /** 是否上架销售。 */
    private boolean onSale;
    /** 审核状态。 */
    private String auditStatus;

    /**
     * 创建商户商品实体。
     */
    public MerchantProduct(Long id, String name, String category, BigDecimal price, Integer stock, String description, boolean onSale, String auditStatus) {
        this.id = id; this.name = name; this.category = category; this.price = price; this.stock = stock; this.description = description; this.onSale = onSale; this.auditStatus = auditStatus;
    }

    /**
     * 复制当前商品并替换上下架状态。
     *
     * @param next 新上下架状态
     * @return 新商户商品实体
     */
    MerchantProduct withSaleState(boolean next) { return new MerchantProduct(id, name, category, price, stock, description, next, auditStatus); }

    /** @return 商品 ID */
    public Long getId() { return id; }
    /** @return 商品名称 */
    public String getName() { return name; }
    /** @return 商品类目 */
    public String getCategory() { return category; }
    /** @return 商品售价 */
    public BigDecimal getPrice() { return price; }
    /** @return 商品库存数量 */
    public Integer getStock() { return stock; }
    /** @return 商品描述 */
    public String getDescription() { return description; }
    /** @return 是否上架销售 */
    public boolean isOnSale() { return onSale; }
    /** @return 审核状态 */
    public String getAuditStatus() { return auditStatus; }
}
