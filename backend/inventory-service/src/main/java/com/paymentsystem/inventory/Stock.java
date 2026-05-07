package com.paymentsystem.inventory;

/**
 * SKU 库存视图。
 */
public class Stock {
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 可用库存数量。 */
    private Integer available;
    /** 已冻结库存数量。 */
    private Integer frozen;

    /**
     * 创建库存视图。
     */
    public Stock(Long skuId, Integer available, Integer frozen) { this.skuId = skuId; this.available = available; this.frozen = frozen; }

    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @return 可用库存数量 */
    public Integer getAvailable() { return available; }
    /** @return 已冻结库存数量 */
    public Integer getFrozen() { return frozen; }
}
