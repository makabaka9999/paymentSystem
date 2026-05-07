package com.paymentsystem.cart.dto;

import java.math.BigDecimal;

/**
 * 购物车商品明细。
 */
public class CartItem {
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 商品名称。 */
    private String productName;
    /** 商品单价。 */
    private BigDecimal price;
    /** 购买数量。 */
    private Integer quantity;

    /**
     * 创建购物车商品明细。
     */
    public CartItem(Long skuId, String productName, BigDecimal price, Integer quantity) { this.skuId = skuId; this.productName = productName; this.price = price; this.quantity = quantity; }

    /**
     * 复制当前明细并替换数量，最小数量为 1。
     *
     * @param nextQuantity 新数量
     * @return 新购物车明细
     */
    public CartItem withQuantity(Integer nextQuantity) { return new CartItem(skuId, productName, price, Math.max(1, nextQuantity)); }

    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @return 商品名称 */
    public String getProductName() { return productName; }
    /** @return 商品单价 */
    public BigDecimal getPrice() { return price; }
    /** @return 购买数量 */
    public Integer getQuantity() { return quantity; }
}
