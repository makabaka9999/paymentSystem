"use client";

import { coupons, products, users } from "./mock-data";
import { CartItem, Order, Product, Role, User } from "./types";

const CART_KEY = "youxuan_cart";
const ORDER_KEY = "youxuan_orders";
const USER_KEY = "youxuan_user";
const FAVORITE_KEY = "youxuan_favorites";
const EXTRA_PRODUCTS_KEY = "youxuan_extra_products";

function read<T>(key: string, fallback: T): T {
  if (typeof window === "undefined") return fallback;
  const raw = window.localStorage.getItem(key);
  if (!raw) return fallback;
  try {
    return JSON.parse(raw) as T;
  } catch {
    return fallback;
  }
}

function write<T>(key: string, value: T) {
  if (typeof window !== "undefined") {
    window.localStorage.setItem(key, JSON.stringify(value));
  }
}

export function allProducts(): Product[] {
  return [...products, ...read<Product[]>(EXTRA_PRODUCTS_KEY, [])];
}

export function publishProduct(product: Omit<Product, "id" | "sales" | "rating" | "stock" | "image" | "tags" | "skus"> & { stock?: number }) {
  const current = read<Product[]>(EXTRA_PRODUCTS_KEY, []);
  const created: Product = {
    ...product,
    id: Date.now(),
    sales: 0,
    rating: 5,
    stock: product.stock ?? 100,
    image: "linear-gradient(135deg,#fb923c,#14b8a6)",
    tags: ["商家新品", "待审核"],
    skus: ["默认规格"],
  };
  write(EXTRA_PRODUCTS_KEY, [created, ...current]);
  return created;
}

export function currentUser(): User | null {
  return read<User | null>(USER_KEY, null);
}

export function login(account: string, password: string, role: Role = "USER"): User {
  if (!account || !password || password.length < 6) {
    throw new Error("请输入账号和至少 6 位密码");
  }
  const seed = users.find((item) => item.role === role) ?? users[0];
  const user = { ...seed, account, name: role === "USER" ? "优选会员" : seed.name };
  write(USER_KEY, user);
  return user;
}

export function logout() {
  if (typeof window !== "undefined") window.localStorage.removeItem(USER_KEY);
}

export function cartItems(): CartItem[] {
  return read<CartItem[]>(CART_KEY, []);
}

export function addToCart(productId: number, sku: string, quantity: number) {
  const items = cartItems();
  const existed = items.find((item) => item.productId === productId && item.sku === sku);
  if (existed) existed.quantity += quantity;
  else items.push({ productId, sku, quantity, selected: true });
  write(CART_KEY, items);
}

export function updateCart(next: CartItem[]) {
  write(CART_KEY, next);
}

export function selectedCartItems() {
  return cartItems().filter((item) => item.selected);
}

export function favoriteIds(): number[] {
  return read<number[]>(FAVORITE_KEY, []);
}

export function toggleFavorite(productId: number) {
  const ids = favoriteIds();
  const next = ids.includes(productId) ? ids.filter((id) => id !== productId) : [...ids, productId];
  write(FAVORITE_KEY, next);
  return next;
}

export function orders(): Order[] {
  return read<Order[]>(ORDER_KEY, []);
}

export function createOrder(payload: Pick<Order, "items" | "address" | "delivery" | "couponCode">): Order {
  const subtotal = payload.items.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const coupon = coupons.find((item) => item.code === payload.couponCode && subtotal >= item.threshold);
  const discount = coupon?.amount ?? 0;
  const order: Order = {
    id: `YX${Date.now()}`,
    userId: currentUser()?.id ?? 1,
    items: payload.items,
    address: payload.address,
    delivery: payload.delivery,
    couponCode: payload.couponCode,
    discount,
    total: Math.max(subtotal - discount, 0),
    status: "待付款",
    createdAt: new Date().toLocaleString("zh-CN"),
  };
  write(ORDER_KEY, [order, ...orders()]);
  return order;
}

export function payOrder(orderId: string) {
  write(
    ORDER_KEY,
    orders().map((order) => (order.id === orderId ? { ...order, status: "待发货" } : order)),
  );
}
