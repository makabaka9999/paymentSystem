# RabbitMQ Topology

- `seckill.request.exchange` -> `seckill.request.queue`: 秒杀请求削峰。
- `order.event.exchange` -> `order.timeout.queue`: 订单超时关闭。
- `payment.success.exchange` -> `order.payment-success.queue`, `inventory.payment-success.queue`, `message.payment-success.queue`: 支付成功事件广播。
- `inventory.compensation.exchange` -> `inventory.rollback.queue`: 秒杀或订单失败后的库存补偿。

MVP 代码先保留接口和配置位，生产化时再把内存事件替换为 `RabbitTemplate` 发布和 `@RabbitListener` 消费。
