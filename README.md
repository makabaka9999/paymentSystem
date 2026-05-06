# 订单支付微服务系统

这是一个 JDK 8 兼容的电商订单支付系统 MVP 骨架，包含 Vue 3 前端、Spring Boot 微服务后端、本地 MySQL 分库脚本，以及 Redis、RabbitMQ、Nacos、Sentinel、Seata 的基础设施配置。

## 技术栈

- 后端：JDK 8、Spring Boot 2.7.18、Spring Cloud 2021.0.8、Spring Cloud Alibaba 2021.0.5.0、Maven 3.8.1
- 前端：Vue 3、Vite、Pinia、Vue Router、Element Plus
- 中间件：本地 MySQL 8 分库、Redis、RabbitMQ、Nacos、Sentinel、Seata

## 模块

- `backend/gateway-service`：统一网关、路由、演示 Token 解析。
- `backend/auth-service`：注册、登录、统一账号多角色。
- `backend/user-service`：用户资料、地址。
- `backend/product-service`：商品列表、商品详情。
- `backend/inventory-service`：库存查询、冻结、扣减、释放。
- `backend/cart-service`：购物车。
- `backend/order-service`：订单创建、查询、取消、支付状态流转。
- `backend/payment-service`：模拟支付单、支付成功/失败、幂等。
- `backend/seckill-service`：秒杀活动、预热、去重、库存预扣、排队记录。
- `backend/merchant-service`：商户商品管理、商户订单视图。
- `backend/admin-service`：用户/商户管理、商品审核、秒杀活动配置。
- `backend/message-service`：站内通知扩展入口。
- `backend/common`：通用响应、枚举、事件、用户上下文。

当前代码先用内存仓储跑通 API 和业务边界，数据库 schema、服务配置和依赖已经按本地 MySQL 分库准备好，后续可把 Controller 内的内存 Map 平滑替换为 Repository/Mapper。

## 本地 MySQL 分库

使用你本机 MySQL，默认连接参数：

```powershell
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=root
```

初始化分库和基础表：

```powershell
mysql -uroot -proot < infra\sql\init-local-mysql.sql
```

如果密码不是 `root`，修改各服务 `application.yml` 的环境变量或启动时传入 `MYSQL_PASSWORD`。

## 启动中间件

MySQL 使用本机安装。其他中间件可用 Docker Compose：

```powershell
docker compose -f infra\docker-compose.yml up -d
```

端口：

- Gateway：`9000`
- Auth：`9001`
- User：`9003`
- Product：`9004`
- Inventory：`9005`
- Cart：`9006`
- Order：`9007`
- Payment：`9008`
- Seckill：`9009`
- Merchant：`9010`
- Admin：`9011`
- Message：`9012`
- Nacos：`8848`
- RabbitMQ Management：`15672`
- Sentinel Dashboard：`8858`

## 启动后端服务

先初始化本地 MySQL 分库，再启动 Redis、RabbitMQ、Nacos、Sentinel、Seata。随后打包并启动需要的服务：

```powershell
mvn -s infra\maven\settings.xml -DskipTests package
java -jar backend\auth-service\target\auth-service-0.1.0-SNAPSHOT.jar
java -jar backend\product-service\target\product-service-0.1.0-SNAPSHOT.jar
java -jar backend\cart-service\target\cart-service-0.1.0-SNAPSHOT.jar
java -jar backend\order-service\target\order-service-0.1.0-SNAPSHOT.jar
java -jar backend\payment-service\target\payment-service-0.1.0-SNAPSHOT.jar
java -jar backend\seckill-service\target\seckill-service-0.1.0-SNAPSHOT.jar
java -jar backend\gateway-service\target\gateway-service-0.1.0-SNAPSHOT.jar
```

也可以按需启动 `user-service`、`inventory-service`、`merchant-service`、`admin-service`、`message-service`。网关端口是 `9000`，前端默认通过它访问后端 API。

## 构建和测试

本项目已按你的本地仓库路径配置 Maven：

```powershell
mvn -s infra\maven\settings.xml test
mvn -s infra\maven\settings.xml -DskipTests package
```

前端：

```powershell
cd frontend
npm --cache .\.npm-cache install
npm --cache .\.npm-cache run build
npm --cache .\.npm-cache run dev
```

前端默认访问 `http://localhost:9000`，开发环境也配置了 `/api` 代理。

## 演示账号

- 普通用户：`user / password`
- 商户：`merchant / password`
- 管理员：`admin / password`

登录后会生成演示 Token，Gateway 会解析 `Bearer demo.{userId}.{role}.{timestamp}` 并传递 `X-User-*` 请求头。

## 秒杀设计落点

- Redis Lua 脚本在 `backend/seckill-service/src/main/resources/lua/seckill_pre_deduct.lua`。
- 内存版接口已实现活动校验、用户去重、库存预扣、排队记录。
- 生产化下一步是把 `submit` 中的内存库存替换成 Redis Lua 执行，把成功请求投递到 `seckill.request.queue`，消费者异步创建订单并做补偿。

## 已验证

- `mvn -s infra\maven\settings.xml test`：通过，JDK 8 编译，3 个 JUnit5 测试通过。
- `mvn -s infra\maven\settings.xml -DskipTests package`：通过，14 个 Maven 模块全部打包成功。
- `npm --cache .\.npm-cache run build`：通过，Vue 前端生产构建成功。
