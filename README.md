# 优选商城 MVP

这是一个原创中文多商家电商平台 MVP，包含消费者端、商家中心和后台管理端。前端已从原 Vue/Vite 改为 Next.js + React + TypeScript + Tailwind CSS；后端继续沿用当前项目已有的 Spring Boot 微服务骨架与 MySQL、Redis、RabbitMQ、Nacos、Sentinel、Seata 中间件。

## 文档

- PRD：[docs/youxuan-mall-prd.md](docs/youxuan-mall-prd.md)
- 页面、组件、后端结构与 API：[docs/youxuan-mall-architecture.md](docs/youxuan-mall-architecture.md)
- MySQL 表结构：[infra/sql/youxuan_mall_schema.sql](infra/sql/youxuan_mall_schema.sql)

## 前端页面

- `/` 首页
- `/products` 商品列表
- `/products/[id]` 商品详情
- `/auth` 或 `/login` 登录/注册
- `/cart` 购物车
- `/checkout` 订单确认
- `/orders` 订单与模拟支付
- `/user` 用户中心
- `/merchant` 商家中心
- `/admin` 后台管理

## 本地运行

```powershell
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5173`。

## 测试账号

- 消费者：`user@example.com / password`
- 商家：`merchant@example.com / password`
- 管理员：`admin@example.com / password`

当前前端 MVP 使用 mock 数据和 localStorage 跑通核心购物流程。后续可将 `frontend/lib/store.ts` 替换为网关 API 调用，接入现有后端服务。
