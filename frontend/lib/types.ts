export type Role = "USER" | "MERCHANT" | "ADMIN";
export type OrderStatus = "待付款" | "待发货" | "待收货" | "已完成";

export interface User {
  id: number;
  name: string;
  account: string;
  role: Role;
}

export interface Shop {
  id: number;
  name: string;
  ownerId: number;
  slogan: string;
  score: number;
  fans: number;
  category: string;
}

export interface Product {
  id: number;
  shopId: number;
  title: string;
  category: string;
  price: number;
  originPrice?: number;
  sales: number;
  rating: number;
  stock: number;
  image: string;
  tags: string[];
  skus: string[];
  description: string;
}

export interface CartItem {
  productId: number;
  sku: string;
  quantity: number;
  selected: boolean;
}

export interface Coupon {
  id: number;
  code: string;
  title: string;
  threshold: number;
  amount: number;
  enabled: boolean;
}

export interface OrderItem {
  productId: number;
  title: string;
  sku: string;
  quantity: number;
  price: number;
}

export interface Order {
  id: string;
  userId: number;
  items: OrderItem[];
  address: string;
  delivery: string;
  couponCode?: string;
  discount: number;
  total: number;
  status: OrderStatus;
  createdAt: string;
}

export interface Review {
  id: number;
  productId: number;
  user: string;
  rating: number;
  content: string;
  createdAt: string;
}
