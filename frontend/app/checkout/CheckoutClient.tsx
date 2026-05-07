"use client";

import { useRouter } from "next/navigation";
import { FormEvent, useEffect, useMemo, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { money } from "@/lib/format";
import { coupons, products } from "@/lib/mock-data";
import { createOrder, selectedCartItems } from "@/lib/store";
import { CartItem } from "@/lib/types";

export function CheckoutClient() {
  const router = useRouter();
  const [items, setItems] = useState<CartItem[]>([]);
  const [couponCode, setCouponCode] = useState("YX30");
  useEffect(() => setItems(selectedCartItems()), []);

  const rows = items.map((item) => ({ ...item, product: products.find((product) => product.id === item.productId)! })).filter((item) => item.product);
  const subtotal = useMemo(() => rows.reduce((sum, item) => sum + item.product.price * item.quantity, 0), [rows]);
  const coupon = coupons.find((item) => item.code === couponCode && subtotal >= item.threshold);
  const total = Math.max(subtotal - (coupon?.amount ?? 0), 0);

  function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const order = createOrder({
      address: String(data.get("address")),
      delivery: String(data.get("delivery")),
      couponCode,
      items: rows.map((item) => ({ productId: item.product.id, title: item.product.title, sku: item.sku, quantity: item.quantity, price: item.product.price })),
    });
    router.push(`/orders?pay=${order.id}`);
  }

  return (
    <AppShell>
      <form onSubmit={submit} className="mx-auto grid max-w-7xl gap-5 px-4 py-8 lg:grid-cols-[1fr_340px]">
        <div className="space-y-5">
          <section className="rounded border border-gray-200 bg-white p-5">
            <h1 className="mb-4 text-2xl font-black">订单确认</h1>
            <label className="block">
              <span className="mb-2 block font-bold">收货地址</span>
              <textarea name="address" required defaultValue="上海市浦东新区优选路 88 号 18F 陈小优 13800138000" className="focus-ring min-h-24 w-full rounded border border-gray-200 px-3 py-3" />
            </label>
          </section>
          <section className="rounded border border-gray-200 bg-white p-5">
            <h2 className="mb-4 text-xl font-black">商品清单</h2>
            <div className="space-y-3">
              {rows.map((item) => (
                <div key={`${item.productId}-${item.sku}`} className="flex items-center justify-between gap-4 border-b border-gray-100 pb-3 last:border-0">
                  <div>
                    <div className="font-bold">{item.product.title}</div>
                    <div className="text-sm text-gray-500">{item.sku} × {item.quantity}</div>
                  </div>
                  <b className="text-brand-600">{money(item.product.price * item.quantity)}</b>
                </div>
              ))}
            </div>
          </section>
          <section className="rounded border border-gray-200 bg-white p-5">
            <h2 className="mb-4 text-xl font-black">配送方式</h2>
            <select name="delivery" className="focus-ring rounded border border-gray-200 px-3 py-2">
              <option>普通快递（免邮）</option>
              <option>次日达（+12 元，MVP 暂不计费）</option>
            </select>
          </section>
        </div>
        <aside className="h-fit rounded border border-gray-200 bg-white p-5 shadow-sm">
          <h2 className="mb-4 text-xl font-black">优惠券</h2>
          <select value={couponCode} onChange={(event) => setCouponCode(event.target.value)} className="focus-ring mb-5 w-full rounded border border-gray-200 px-3 py-2">
            {coupons.map((item) => <option key={item.id} value={item.code}>{item.title}</option>)}
          </select>
          <div className="space-y-3 text-sm">
            <div className="flex justify-between"><span>商品金额</span><b>{money(subtotal)}</b></div>
            <div className="flex justify-between"><span>优惠抵扣</span><b>- {money(coupon?.amount ?? 0)}</b></div>
            <div className="flex justify-between border-t border-gray-100 pt-3 text-lg"><span>订单总价</span><b className="text-2xl text-brand-600">{money(total)}</b></div>
          </div>
          <button className="focus-ring mt-5 w-full rounded bg-brand-500 px-4 py-3 font-bold text-white">提交订单</button>
        </aside>
      </form>
    </AppShell>
  );
}
