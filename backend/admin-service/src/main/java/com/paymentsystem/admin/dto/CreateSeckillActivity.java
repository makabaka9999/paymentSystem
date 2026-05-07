package com.paymentsystem.admin.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 管理后台创建秒杀活动请求参数。
 */
public class CreateSeckillActivity {
    /** 商品 ID。 */
    private Long productId;
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 秒杀活动标题。 */
    private String title;
    /** 秒杀价格。 */
    private BigDecimal seckillPrice;
    /** 秒杀库存数量。 */
    private Integer stock;
    /** 活动开始时间。 */
    private Instant startsAt;
    /** 活动结束时间。 */
    private Instant endsAt;

    /** @return 商品 ID */
    public Long getProductId() { return productId; }
    /** @param productId 商品 ID */
    public void setProductId(Long productId) { this.productId = productId; }
    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @param skuId 商品 SKU ID */
    public void setSkuId(Long skuId) { this.skuId = skuId; }
    /** @return 秒杀活动标题 */
    public String getTitle() { return title; }
    /** @param title 秒杀活动标题 */
    public void setTitle(String title) { this.title = title; }
    /** @return 秒杀价格 */
    public BigDecimal getSeckillPrice() { return seckillPrice; }
    /** @param seckillPrice 秒杀价格 */
    public void setSeckillPrice(BigDecimal seckillPrice) { this.seckillPrice = seckillPrice; }
    /** @return 秒杀库存数量 */
    public Integer getStock() { return stock; }
    /** @param stock 秒杀库存数量 */
    public void setStock(Integer stock) { this.stock = stock; }
    /** @return 活动开始时间 */
    public Instant getStartsAt() { return startsAt; }
    /** @param startsAt 活动开始时间 */
    public void setStartsAt(Instant startsAt) { this.startsAt = startsAt; }
    /** @return 活动结束时间 */
    public Instant getEndsAt() { return endsAt; }
    /** @param endsAt 活动结束时间 */
    public void setEndsAt(Instant endsAt) { this.endsAt = endsAt; }
}
