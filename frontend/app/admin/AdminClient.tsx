"use client";

import { BarChart3, CheckCircle2, LucideIcon, ShieldAlert, Store, Users } from "lucide-react";
import { useEffect, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { products, shops, users } from "@/lib/mock-data";
import { allProducts, orders } from "@/lib/store";
import { Order, Product } from "@/lib/types";

export function AdminClient() {
  const [productItems, setProductItems] = useState<Product[]>(products);
  const [orderItems, setOrderItems] = useState<Order[]>([]);
  useEffect(() => {
    setProductItems(allProducts());
    setOrderItems(orders());
  }, []);

  const stats: Array<[string, number, LucideIcon]> = [
    ["用户数", users.length, Users],
    ["商家数", shops.length, Store],
    ["商品数", productItems.length, BarChart3],
    ["订单数", orderItems.length, CheckCircle2],
  ];

  return (
    <AppShell>
      <div className="mx-auto max-w-7xl px-4 py-8">
        <h1 className="mb-5 text-3xl font-black">后台管理</h1>
        <div className="mb-5 grid gap-4 md:grid-cols-4">
          {stats.map(([label, value, Icon]) => (
            <div key={label} className="rounded border border-gray-200 bg-white p-5 shadow-sm">
              <Icon className="mb-3 text-brand-600" size={24} />
              <div className="text-sm text-gray-500">{label}</div>
              <b className="text-3xl">{value}</b>
            </div>
          ))}
        </div>
        <div className="grid gap-5 lg:grid-cols-2">
          <section className="rounded border border-gray-200 bg-white p-5">
            <h2 className="mb-4 text-xl font-black">用户管理</h2>
            {users.map((user) => <div key={user.id} className="flex justify-between border-b border-gray-100 py-3 last:border-0"><span>{user.name}</span><b>{user.role}</b></div>)}
          </section>
          <section className="rounded border border-gray-200 bg-white p-5">
            <h2 className="mb-4 text-xl font-black">商家管理</h2>
            {shops.map((shop) => <div key={shop.id} className="flex justify-between border-b border-gray-100 py-3 last:border-0"><span>{shop.name}</span><b>{shop.score}</b></div>)}
          </section>
          <section className="rounded border border-gray-200 bg-white p-5">
            <h2 className="mb-4 text-xl font-black">商品审核</h2>
            {productItems.slice(0, 6).map((product) => (
              <div key={product.id} className="flex items-center justify-between gap-3 border-b border-gray-100 py-3 last:border-0">
                <span>{product.title}</span>
                <button className="rounded bg-emerald-50 px-3 py-2 text-sm font-bold text-emerald-700">通过</button>
              </div>
            ))}
          </section>
          <section className="rounded border border-gray-200 bg-white p-5">
            <h2 className="mb-4 text-xl font-black">订单管理</h2>
            {orderItems.length ? orderItems.map((order) => <div key={order.id} className="flex justify-between border-b border-gray-100 py-3 last:border-0"><span>{order.id}</span><b>{order.status}</b></div>) : <p className="text-gray-500">暂无订单数据。</p>}
          </section>
          <section className="rounded border border-gray-200 bg-white p-5 lg:col-span-2">
            <h2 className="mb-4 flex items-center gap-2 text-xl font-black"><ShieldAlert className="text-brand-600" /> 数据统计</h2>
            <div className="grid gap-3 md:grid-cols-4">
              {["成交金额", "支付转化", "客单价", "待审核"].map((item, index) => (
                <div key={item} className="rounded bg-gray-50 p-4">
                  <div className="text-sm text-gray-500">{item}</div>
                  <b className="text-2xl">{index === 0 ? "¥12,860" : index === 1 ? "68%" : index === 2 ? "¥186" : "4"}</b>
                </div>
              ))}
            </div>
          </section>
        </div>
      </div>
    </AppShell>
  );
}
