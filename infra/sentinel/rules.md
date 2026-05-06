# Sentinel Rules

- 网关：`/api/seckill/**` 按 QPS 限流，默认 1000/s。
- 秒杀提交：按 `activityId + userId + IP` 做热点参数限流。
- 商品详情：按 `productId` 做热点参数保护，缓存未命中时熔断到降级文案。
- 支付和订单写接口：低 QPS 保护，避免下游 MySQL 抖动扩大。
