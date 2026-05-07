"use client";

import { FormEvent, useEffect, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { money } from "@/lib/format";
import { categories, shops } from "@/lib/mock-data";
import { allProducts, orders, publishProduct } from "@/lib/store";
import { Order, Product } from "@/lib/types";

export function MerchantClient() {
  const [items, setItems] = useState<Product[]>([]);
  const [orderItems, setOrderItems] = useState<Order[]>([]);
  const shop = shops[0];

  useEffect(() => {
    setItems(allProducts().filter((item) => item.shopId === shop.id));
    setOrderItems(orders());
  }, [shop.id]);

  function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    publishProduct({
      shopId: shop.id,
      title: String(data.get("title")),
      category: String(data.get("category")),
      price: Number(data.get("price")),
      originPrice: Number(data.get("originPrice")) || undefined,
      stock: Number(data.get("stock")) || 100,
      description: String(data.get("description")),
    });
    setItems(allProducts().filter((item) => item.shopId === shop.id));
    event.currentTarget.reset();
  }

  return (
    <AppShell>
      <div className="mx-auto max-w-7xl px-4 py-8">
        <h1 className="mb-5 text-3xl font-black">商家中心</h1>
        <div className="mb-5 grid gap-4 md:grid-cols-3">
          <div className="rounded border border-gray-200 bg-white p-5"><div className="text-sm text-gray-500">店铺评分</div><b className="text-3xl text-brand-600">{shop.score}</b></div>
          <div className="rounded border border-gray-200 bg-white p-5"><div className="text-sm text-gray-500">商品数量</div><b className="text-3xl">{items.length}</b></div>
          <div className="rounded border border-gray-200 bg-white p-5"><div className="text-sm text-gray-500">待处理订单</div><b className="text-3xl">{orderItems.filter((item) => item.status === "待发货").length}</b></div>
        </div>
        <div className="grid gap-5 lg:grid-cols-[420px_1fr]">
          <form onSubmit={submit} className="h-fit rounded border border-gray-200 bg-white p-5">
            <h2 className="mb-4 text-xl font-black">商品发布</h2>
            <div className="grid gap-3">
              <input name="title" required placeholder="商品标题" className="focus-ring rounded border border-gray-200 px-3 py-2" />
              <select name="category" className="focus-ring rounded border border-gray-200 px-3 py-2">{categories.map((item) => <option key={item}>{item}</option>)}</select>
              <div className="grid grid-cols-3 gap-3">
                <input name="price" required type="number" placeholder="售价" className="focus-ring rounded border border-gray-200 px-3 py-2" />
                <input name="originPrice" type="number" placeholder="原价" className="focus-ring rounded border border-gray-200 px-3 py-2" />
                <input name="stock" type="number" placeholder="库存" className="focus-ring rounded border border-gray-200 px-3 py-2" />
              </div>
              <textarea name="description" required placeholder="商品详情介绍" className="focus-ring min-h-24 rounded border border-gray-200 px-3 py-2" />
              <button className="focus-ring rounded bg-brand-500 px-4 py-3 font-bold text-white">发布商品</button>
            </div>
          </form>
          <div className="space-y-5">
            <section className="rounded border border-gray-200 bg-white p-5">
              <h2 className="mb-4 text-xl font-black">商品管理</h2>
              <div className="space-y-3">
                {items.map((item) => (
                  <div key={item.id} className="flex flex-wrap items-center justify-between gap-3 rounded bg-gray-50 p-3">
                    <div><b>{item.title}</b><div className="text-sm text-gray-500">{item.category} · 库存 {item.stock}</div></div>
                    <div className="font-black text-brand-600">{money(item.price)}</div>
                  </div>
                ))}
              </div>
            </section>
            <section className="rounded border border-gray-200 bg-white p-5">
              <h2 className="mb-4 text-xl font-black">订单管理</h2>
              <div className="space-y-2 text-sm">
                {orderItems.slice(0, 5).map((order) => <div key={order.id} className="flex justify-between rounded bg-gray-50 p-3"><span>{order.id}</span><b>{order.status}</b></div>)}
              </div>
            </section>
            <section className="rounded border border-gray-200 bg-white p-5">
              <h2 className="mb-4 text-xl font-black">店铺信息管理</h2>
              <input defaultValue={shop.name} className="focus-ring mb-3 w-full rounded border border-gray-200 px-3 py-2" />
              <textarea defaultValue={shop.slogan} className="focus-ring min-h-20 w-full rounded border border-gray-200 px-3 py-2" />
            </section>
          </div>
        </div>
      </div>
    </AppShell>
  );
}
