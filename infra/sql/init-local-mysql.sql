-- 数据库：auth_db，认证中心库，保存登录账号、角色和认证相关数据。
CREATE DATABASE IF NOT EXISTS auth_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：user_db，用户中心库，保存用户资料、地址等用户域数据。
CREATE DATABASE IF NOT EXISTS user_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：product_db，商品中心库，保存商品、SKU、分类和商品审核状态。
CREATE DATABASE IF NOT EXISTS product_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：inventory_db，库存中心库，保存 SKU 可用库存、冻结库存和版本号。
CREATE DATABASE IF NOT EXISTS inventory_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：cart_db，购物车库，保存用户购物车明细。
CREATE DATABASE IF NOT EXISTS cart_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：order_db，订单中心库，保存订单主表和订单明细。
CREATE DATABASE IF NOT EXISTS order_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：payment_db，支付中心库，保存模拟支付单和支付幂等信息。
CREATE DATABASE IF NOT EXISTS payment_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：seckill_db，秒杀中心库，保存秒杀活动和用户秒杀请求记录。
CREATE DATABASE IF NOT EXISTS seckill_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：merchant_db，商户中心库，保存商户主体资料和状态。
CREATE DATABASE IF NOT EXISTS merchant_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
-- 数据库：admin_db，管理后台库，保存平台管理操作审计记录。
CREATE DATABASE IF NOT EXISTS admin_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE auth_db;
CREATE TABLE IF NOT EXISTS user_account (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账号主键ID',
  username VARCHAR(64) NOT NULL UNIQUE COMMENT '登录用户名，全局唯一',
  password_hash VARCHAR(128) NOT NULL COMMENT '登录密码哈希值，演示环境可存明文占位',
  role VARCHAR(32) NOT NULL COMMENT '用户角色：USER普通用户、MERCHANT商户、ADMIN管理员',
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE' COMMENT '账号状态：ACTIVE启用、DISABLED禁用',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号创建时间'
) COMMENT='用户账号表，保存统一账号、多角色和登录凭据';
INSERT IGNORE INTO user_account(id, username, password_hash, role) VALUES
(1, 'user', 'password', 'USER'),
(2, 'merchant', 'password', 'MERCHANT'),
(3, 'admin', 'password', 'ADMIN');

USE user_db;
CREATE TABLE IF NOT EXISTS user_profile (
  id BIGINT PRIMARY KEY COMMENT '用户ID，对应认证中心账号ID',
  username VARCHAR(64) NOT NULL COMMENT '用户名冗余字段，用于用户资料展示',
  nickname VARCHAR(64) COMMENT '用户昵称',
  phone VARCHAR(32) COMMENT '用户手机号',
  role VARCHAR(32) NOT NULL COMMENT '用户角色：USER普通用户、MERCHANT商户、ADMIN管理员',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户资料创建时间'
) COMMENT='用户资料表，保存用户基础信息';
CREATE TABLE IF NOT EXISTS user_address (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '收货地址主键ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  contact_name VARCHAR(64) NOT NULL COMMENT '收货联系人姓名',
  phone VARCHAR(32) NOT NULL COMMENT '收货联系人手机号',
  detail VARCHAR(255) NOT NULL COMMENT '详细收货地址',
  default_address TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认地址：0否、1是'
) COMMENT='用户收货地址表，保存下单配送地址';

USE product_db;
CREATE TABLE IF NOT EXISTS product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品主键ID',
  merchant_id BIGINT NOT NULL COMMENT '所属商户ID',
  name VARCHAR(128) NOT NULL COMMENT '商品名称',
  category VARCHAR(64) NOT NULL COMMENT '商品分类名称',
  description VARCHAR(512) COMMENT '商品详情描述',
  audit_status VARCHAR(32) NOT NULL DEFAULT 'PENDING_APPROVAL' COMMENT '审核状态：PENDING_APPROVAL待审核、APPROVED已通过、REJECTED已拒绝',
  on_sale TINYINT NOT NULL DEFAULT 0 COMMENT '是否上架销售：0否、1是',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '商品创建时间'
) COMMENT='商品主表，保存商品基础信息、审核和上下架状态';
CREATE TABLE IF NOT EXISTS product_sku (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SKU主键ID',
  product_id BIGINT NOT NULL COMMENT '所属商品ID',
  sku_name VARCHAR(128) NOT NULL COMMENT 'SKU规格名称',
  price DECIMAL(12,2) NOT NULL COMMENT 'SKU销售价格'
) COMMENT='商品SKU表，保存商品规格和价格';
INSERT IGNORE INTO product(id, merchant_id, name, category, description, audit_status, on_sale) VALUES
(101, 2, '机械键盘 Pro', '办公外设', '适合高频输入的热插拔键盘', 'APPROVED', 1),
(102, 2, '无线降噪耳机', '数码音频', '通勤和办公两用', 'APPROVED', 1),
(103, 2, '人体工学椅', '办公家具', '长时间工作支撑', 'APPROVED', 1);
INSERT IGNORE INTO product_sku(id, product_id, sku_name, price) VALUES
(10001, 101, '默认款', 399.00),
(10002, 102, '默认款', 699.00),
(10003, 103, '默认款', 1299.00);

USE inventory_db;
CREATE TABLE IF NOT EXISTS sku_stock (
  sku_id BIGINT PRIMARY KEY COMMENT 'SKU ID，对应商品SKU表主键',
  available INT NOT NULL COMMENT '可售库存数量',
  frozen INT NOT NULL DEFAULT 0 COMMENT '已冻结库存数量，待支付订单占用',
  version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号，用于并发扣减控制'
) COMMENT='SKU库存表，保存可用库存、冻结库存和并发控制版本';
INSERT IGNORE INTO sku_stock(sku_id, available, frozen) VALUES
(10001, 300, 0), (10002, 160, 0), (10003, 48, 0);

USE cart_db;
CREATE TABLE IF NOT EXISTS cart_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车明细主键ID',
  user_id BIGINT NOT NULL COMMENT '用户ID',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  product_name VARCHAR(128) NOT NULL COMMENT '商品名称快照',
  price DECIMAL(12,2) NOT NULL COMMENT '加入购物车时的商品价格快照',
  quantity INT NOT NULL COMMENT '购物车商品数量',
  UNIQUE KEY uk_user_sku(user_id, sku_id)
) COMMENT='购物车明细表，保存用户待购买商品';

USE order_db;
CREATE TABLE IF NOT EXISTS order_main (
  id BIGINT PRIMARY KEY COMMENT '订单主键ID',
  user_id BIGINT NOT NULL COMMENT '下单用户ID',
  total_amount DECIMAL(12,2) NOT NULL COMMENT '订单总金额',
  status VARCHAR(32) NOT NULL COMMENT '订单状态：CREATED待支付、PAYING支付中、PAID已支付、CANCELLED已取消、CLOSED已关闭、SHIPPED已发货、FINISHED已完成、REFUNDED已退款',
  address_id VARCHAR(64) COMMENT '收货地址ID或地址快照标识',
  created_at DATETIME NOT NULL COMMENT '订单创建时间',
  paid_at DATETIME NULL COMMENT '订单支付成功时间'
) COMMENT='订单主表，保存订单金额、状态和用户信息';
CREATE TABLE IF NOT EXISTS order_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单明细主键ID',
  order_id BIGINT NOT NULL COMMENT '所属订单ID',
  sku_id BIGINT NOT NULL COMMENT 'SKU ID',
  product_name VARCHAR(128) NOT NULL COMMENT '商品名称快照',
  price DECIMAL(12,2) NOT NULL COMMENT '下单时商品单价快照',
  quantity INT NOT NULL COMMENT '购买数量'
) COMMENT='订单明细表，保存订单内商品快照';

USE payment_db;
CREATE TABLE IF NOT EXISTS payment_order (
  payment_no VARCHAR(64) PRIMARY KEY COMMENT '支付单号，全局唯一',
  order_id BIGINT NOT NULL COMMENT '关联订单ID',
  user_id BIGINT NOT NULL COMMENT '支付用户ID',
  amount DECIMAL(12,2) NOT NULL COMMENT '支付金额',
  status VARCHAR(32) NOT NULL COMMENT '支付状态：INIT初始化、PROCESSING处理中、SUCCESS成功、FAILED失败、CLOSED关闭',
  idempotency_key VARCHAR(128) NOT NULL UNIQUE COMMENT '幂等键，用于防止重复创建或重复回调',
  created_at DATETIME NOT NULL COMMENT '支付单创建时间',
  paid_at DATETIME NULL COMMENT '支付成功时间'
) COMMENT='支付单表，保存模拟支付记录和幂等信息';

USE seckill_db;
CREATE TABLE IF NOT EXISTS seckill_activity (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '秒杀活动主键ID',
  product_id BIGINT NOT NULL COMMENT '参与秒杀的商品ID',
  sku_id BIGINT NOT NULL COMMENT '参与秒杀的SKU ID',
  title VARCHAR(128) NOT NULL COMMENT '秒杀活动标题',
  seckill_price DECIMAL(12,2) NOT NULL COMMENT '秒杀活动价格',
  stock INT NOT NULL COMMENT '秒杀活动库存数量',
  starts_at DATETIME NOT NULL COMMENT '秒杀开始时间',
  ends_at DATETIME NOT NULL COMMENT '秒杀结束时间',
  enabled TINYINT NOT NULL DEFAULT 1 COMMENT '活动是否启用：0否、1是'
) COMMENT='秒杀活动表，保存活动时间、价格和库存配置';
CREATE TABLE IF NOT EXISTS seckill_record (
  request_id VARCHAR(64) PRIMARY KEY COMMENT '秒杀请求ID，用于用户查询排队结果',
  activity_id BIGINT NOT NULL COMMENT '秒杀活动ID',
  user_id BIGINT NOT NULL COMMENT '参与秒杀的用户ID',
  sku_id BIGINT NOT NULL COMMENT '秒杀SKU ID',
  status VARCHAR(32) NOT NULL COMMENT '秒杀请求状态：QUEUED排队中、SUCCESS成功、FAILED失败',
  message VARCHAR(255) COMMENT '秒杀处理结果说明',
  created_at DATETIME NOT NULL COMMENT '秒杀请求创建时间',
  UNIQUE KEY uk_activity_user(activity_id, user_id)
) COMMENT='秒杀请求记录表，保存用户秒杀排队和处理结果';

USE merchant_db;
CREATE TABLE IF NOT EXISTS merchant (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商户主键ID',
  user_id BIGINT NOT NULL UNIQUE COMMENT '关联用户账号ID',
  name VARCHAR(128) NOT NULL COMMENT '商户名称',
  status VARCHAR(32) NOT NULL DEFAULT 'APPROVED' COMMENT '商户状态：PENDING待审核、APPROVED已通过、DISABLED已禁用'
) COMMENT='商户主体表，保存商户资料和审核状态';

USE admin_db;
CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '审计日志主键ID',
  operator_id BIGINT NOT NULL COMMENT '操作人用户ID',
  target_type VARCHAR(64) NOT NULL COMMENT '操作对象类型，例如PRODUCT、MERCHANT、SECKILL_ACTIVITY',
  target_id BIGINT NOT NULL COMMENT '操作对象ID',
  action VARCHAR(64) NOT NULL COMMENT '操作动作，例如APPROVE、REJECT、CREATE、UPDATE',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作发生时间'
) COMMENT='后台审计日志表，保存管理员关键操作记录';
