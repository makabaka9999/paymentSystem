package com.paymentsystem.inventory.dto;

/**
 * 库存操作请求参数。
 */
public class StockCommand {
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 操作库存数量。 */
    private Integer quantity;
    /** 业务单号，用于调用方追踪库存操作来源。 */
    private String bizNo;

    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @param skuId 商品 SKU ID */
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    /** @return 操作库存数量，未传时默认 0 */
    public Integer getQuantity() { return quantity == null ? 0 : quantity; }
    /** @param quantity 操作库存数量 */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    /** @return 业务单号 */
    public String getBizNo() { return bizNo; }
    /** @param bizNo 业务单号 */
    public void setBizNo(String bizNo) { this.bizNo = bizNo; }
}
