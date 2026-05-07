package com.paymentsystem.order.dto;

import java.math.BigDecimal;

/**
 * 订单商品明细。
 */
public class OrderItem {
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 商品名称。 */
    private String productName;
    /** 商品成交单价。 */
    private BigDecimal price;
    /** 购买数量。 */
    private Integer quantity;

    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }

    /** @param skuId 商品 SKU ID */
    public void setSkuId(Long skuId) { this.skuId = skuId; }

    /** @return 商品名称 */
    public String getProductName() { return productName; }

    /** @param productName 商品名称 */
    public void setProductName(String productName) { this.productName = productName; }

    /** @return 商品成交单价，未传时返回 0 */
    public BigDecimal getPrice() { return price == null ? BigDecimal.ZERO : price; }

    /** @param price 商品成交单价 */
    public void setPrice(BigDecimal price) { this.price = price; }

    /** @return 购买数量，未传时默认 1 */
    public Integer getQuantity() { return quantity == null ? 1 : quantity; }

    /** @param quantity 购买数量 */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
