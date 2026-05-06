CREATE DATABASE IF NOT EXISTS auth_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS user_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS product_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS inventory_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS cart_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS order_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS payment_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS seckill_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS merchant_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS admin_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE auth_db;
CREATE TABLE IF NOT EXISTS user_account (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(128) NOT NULL,
  role VARCHAR(32) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
INSERT IGNORE INTO user_account(id, username, password_hash, role) VALUES
(1, 'user', 'password', 'USER'),
(2, 'merchant', 'password', 'MERCHANT'),
(3, 'admin', 'password', 'ADMIN');

USE user_db;
CREATE TABLE IF NOT EXISTS user_profile (
  id BIGINT PRIMARY KEY,
  username VARCHAR(64) NOT NULL,
  nickname VARCHAR(64),
  phone VARCHAR(32),
  role VARCHAR(32) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS user_address (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  contact_name VARCHAR(64) NOT NULL,
  phone VARCHAR(32) NOT NULL,
  detail VARCHAR(255) NOT NULL,
  default_address TINYINT NOT NULL DEFAULT 0
);

USE product_db;
CREATE TABLE IF NOT EXISTS product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  merchant_id BIGINT NOT NULL,
  name VARCHAR(128) NOT NULL,
  category VARCHAR(64) NOT NULL,
  description VARCHAR(512),
  audit_status VARCHAR(32) NOT NULL DEFAULT 'PENDING_APPROVAL',
  on_sale TINYINT NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS product_sku (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  sku_name VARCHAR(128) NOT NULL,
  price DECIMAL(12,2) NOT NULL
);
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
  sku_id BIGINT PRIMARY KEY,
  available INT NOT NULL,
  frozen INT NOT NULL DEFAULT 0,
  version INT NOT NULL DEFAULT 0
);
INSERT IGNORE INTO sku_stock(sku_id, available, frozen) VALUES
(10001, 300, 0), (10002, 160, 0), (10003, 48, 0);

USE cart_db;
CREATE TABLE IF NOT EXISTS cart_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  sku_id BIGINT NOT NULL,
  product_name VARCHAR(128) NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  quantity INT NOT NULL,
  UNIQUE KEY uk_user_sku(user_id, sku_id)
);

USE order_db;
CREATE TABLE IF NOT EXISTS order_main (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  total_amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(32) NOT NULL,
  address_id VARCHAR(64),
  created_at DATETIME NOT NULL,
  paid_at DATETIME NULL
);
CREATE TABLE IF NOT EXISTS order_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  sku_id BIGINT NOT NULL,
  product_name VARCHAR(128) NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  quantity INT NOT NULL
);

USE payment_db;
CREATE TABLE IF NOT EXISTS payment_order (
  payment_no VARCHAR(64) PRIMARY KEY,
  order_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(32) NOT NULL,
  idempotency_key VARCHAR(128) NOT NULL UNIQUE,
  created_at DATETIME NOT NULL,
  paid_at DATETIME NULL
);

USE seckill_db;
CREATE TABLE IF NOT EXISTS seckill_activity (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  product_id BIGINT NOT NULL,
  sku_id BIGINT NOT NULL,
  title VARCHAR(128) NOT NULL,
  seckill_price DECIMAL(12,2) NOT NULL,
  stock INT NOT NULL,
  starts_at DATETIME NOT NULL,
  ends_at DATETIME NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1
);
CREATE TABLE IF NOT EXISTS seckill_record (
  request_id VARCHAR(64) PRIMARY KEY,
  activity_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  sku_id BIGINT NOT NULL,
  status VARCHAR(32) NOT NULL,
  message VARCHAR(255),
  created_at DATETIME NOT NULL,
  UNIQUE KEY uk_activity_user(activity_id, user_id)
);

USE merchant_db;
CREATE TABLE IF NOT EXISTS merchant (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  name VARCHAR(128) NOT NULL,
  status VARCHAR(32) NOT NULL DEFAULT 'APPROVED'
);

USE admin_db;
CREATE TABLE IF NOT EXISTS audit_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  operator_id BIGINT NOT NULL,
  target_type VARCHAR(64) NOT NULL,
  target_id BIGINT NOT NULL,
  action VARCHAR(64) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

