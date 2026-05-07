# 优选商城页面、组件与接口设计

## 页面结构

- `/` 首页：顶部导航、搜索、分类、Banner、推荐商品、热门店铺、限时优惠、底部信息栏。
- `/products` 商品列表：分类、搜索结果、价格筛选、销量排序、评分排序、商品卡片。
- `/products/[id]` 商品详情：图片、标题、价格、销量、SKU、数量、加购、购买、详情、评价。
- `/auth` 登录注册：手机号/邮箱、密码、注册确认、角色切换、校验。
- `/cart` 购物车：勾选、数量、删除、总价、结算。
- `/checkout` 订单确认：地址、商品清单、优惠券、配送方式、总价、提交。
- `/orders` 我的订单与模拟支付。
- `/user` 用户中心：订单、收藏、地址、资料。
- `/merchant` 商家中心：发布、商品管理、订单管理、店铺信息。
- `/admin` 后台管理：用户、商家、商品审核、订单、统计。

## 前端组件结构

```text
frontend/
  app/
    page.tsx
    products/page.tsx
    products/[id]/page.tsx
    auth/page.tsx
    cart/page.tsx
    checkout/page.tsx
    orders/page.tsx
    user/page.tsx
    merchant/page.tsx
    admin/page.tsx
  components/
    AppShell.tsx
    Header.tsx
    Footer.tsx
    ProductCard.tsx
    SectionTitle.tsx
  lib/
    mock-data.ts
    store.ts
    types.ts
    format.ts
```

## 后端项目结构建议

当前项目已存在 Spring Boot 微服务和 MySQL、Redis、RabbitMQ、Nacos、Sentinel、Seata 中间件。MVP 可继续沿用：

```text
backend/
  gateway-service      统一网关、鉴权头解析、路由
  auth-service         登录注册、JWT
  user-service         用户资料、地址、收藏
  product-service      商品、分类、搜索、评价
  cart-service         购物车
  order-service        下单、订单状态
  payment-service      模拟支付
  merchant-service     商家店铺、商品发布、订单处理
  admin-service        审核、用户/商家/订单管理、统计
  common               通用响应、枚举、用户上下文
```

## API 接口设计

### 认证

- `POST /api/auth/register` 注册：`account,password,role`
- `POST /api/auth/login` 登录：`account,password`
- `GET /api/auth/me` 当前用户

### 商品

- `GET /api/products?q=&category=&minPrice=&maxPrice=&sort=` 商品列表
- `GET /api/products/{id}` 商品详情
- `POST /api/merchant/products` 商家发布商品
- `PUT /api/merchant/products/{id}` 修改商品
- `POST /api/admin/products/{id}/approve` 商品审核通过
- `POST /api/admin/products/{id}/reject` 商品审核驳回

### 购物车

- `GET /api/cart` 获取购物车
- `POST /api/cart/items` 加入购物车：`productId,skuId,quantity`
- `PUT /api/cart/items/{id}` 修改数量/勾选
- `DELETE /api/cart/items/{id}` 删除

### 订单与支付

- `POST /api/orders` 创建订单：`addressId,items,couponId,deliveryMethod`
- `GET /api/orders` 我的订单
- `GET /api/orders/{id}` 订单详情
- `POST /api/payments/mock` 模拟支付：`orderId`
- `POST /api/orders/{id}/ship` 商家发货
- `POST /api/orders/{id}/receive` 用户确认收货

### 用户

- `GET /api/users/me/favorites` 收藏列表
- `POST /api/users/me/favorites/{productId}` 收藏商品
- `DELETE /api/users/me/favorites/{productId}` 取消收藏
- `GET /api/users/me/addresses` 地址列表
- `POST /api/users/me/addresses` 新增地址
- `POST /api/products/{id}/reviews` 评价商品

### 优惠券

- `GET /api/coupons/available` 可用优惠券
- `POST /api/coupons/{id}/claim` 领取优惠券

## 本地运行

```powershell
cd frontend
npm install
npm run dev
```

访问 `http://localhost:5173`。
