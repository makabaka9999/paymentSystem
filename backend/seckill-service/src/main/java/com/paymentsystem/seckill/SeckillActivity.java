package com.paymentsystem.seckill;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * 秒杀活动实体。
 */
public class SeckillActivity {
    /** 活动 ID。 */
    private Long id;
    /** 商品 ID。 */
    private Long productId;
    /** 商品 SKU ID。 */
    private Long skuId;
    /** 活动标题。 */
    private String title;
    /** 秒杀价格。 */
    private BigDecimal seckillPrice;
    /** 活动库存数量。 */
    private Integer stock;
    /** 活动开始时间。 */
    private Instant startsAt;
    /** 活动结束时间。 */
    private Instant endsAt;
    /** 是否启用活动。 */
    private boolean enabled;

    /**
     * 创建秒杀活动实体。
     */
    public SeckillActivity(Long id, Long productId, Long skuId, String title, BigDecimal seckillPrice, Integer stock, Instant startsAt, Instant endsAt, boolean enabled) {
        this.id = id; this.productId = productId; this.skuId = skuId; this.title = title; this.seckillPrice = seckillPrice; this.stock = stock; this.startsAt = startsAt; this.endsAt = endsAt; this.enabled = enabled;
    }

    /**
     * 判断活动在指定时间是否可参与。
     *
     * @param now 当前时间
     * @return 是否可参与
     */
    boolean isActive(Instant now) { return enabled && !now.isBefore(startsAt) && now.isBefore(endsAt); }

    /** @return 活动 ID */
    public Long getId() { return id; }
    /** @return 商品 ID */
    public Long getProductId() { return productId; }
    /** @return 商品 SKU ID */
    public Long getSkuId() { return skuId; }
    /** @return 活动标题 */
    public String getTitle() { return title; }
    /** @return 秒杀价格 */
    public BigDecimal getSeckillPrice() { return seckillPrice; }
    /** @return 活动库存数量 */
    public Integer getStock() { return stock; }
    /** @return 活动开始时间 */
    public Instant getStartsAt() { return startsAt; }
    /** @return 活动结束时间 */
    public Instant getEndsAt() { return endsAt; }
    /** @return 是否启用活动 */
    public boolean isEnabled() { return enabled; }
}
