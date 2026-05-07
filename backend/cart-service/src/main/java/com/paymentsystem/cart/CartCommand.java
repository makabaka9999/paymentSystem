package com.paymentsystem.cart;

import java.math.BigDecimal;

/**
 * 购物车商品操作请求参数。
 */
public class CartCommand {
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 商品名称。 */
    private String productName;
    /** 商品单价。 */
    private BigDecimal price;
    /** 购买数量。 */
    private Integer quantity;

    /**
     * 转换为购物车商品明细。
     *
     * @return 购物车商品明细
     */
    CartItem toItem() { return new CartItem(skuId, productName, price, getQuantity()); }

    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @param skuId 商品 SKU ID */
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    /** @return 商品名称 */
    public String getProductName() { return productName; }
    /** @param productName 商品名称 */
    public void setProductName(String productName) { this.productName = productName; }
    /** @return 商品单价 */
    public BigDecimal getPrice() { return price; }
    /** @param price 商品单价 */
    public void setPrice(BigDecimal price) { this.price = price; }
    /** @return 购买数量，未传时默认 1 */
    public Integer getQuantity() { return quantity == null ? 1 : quantity; }
    /** @param quantity 购买数量 */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
