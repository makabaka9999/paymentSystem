"use client";

import { useSearchParams } from "next/navigation";
import { CreditCard } from "lucide-react";
import { useEffect, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { money } from "@/lib/format";
import { orders as readOrders, payOrder } from "@/lib/store";
import { Order } from "@/lib/types";

export function OrdersClient() {
  const searchParams = useSearchParams();
  const [items, setItems] = useState<Order[]>([]);
  useEffect(() => setItems(readOrders()), []);

  function pay(id: string) {
    payOrder(id);
    setItems(readOrders());
  }

  return (
    <AppShell>
      <div className="mx-auto max-w-7xl px-4 py-8">
        <h1 className="mb-5 text-3xl font-black">我的订单</h1>
        {searchParams.get("pay") && <div className="mb-4 rounded bg-orange-50 px-4 py-3 text-brand-700">订单已创建，可点击模拟支付完成付款。</div>}
        <div className="space-y-4">
          {items.map((order) => (
            <section key={order.id} className="rounded border border-gray-200 bg-white p-5 shadow-sm">
              <div className="mb-4 flex flex-wrap items-center justify-between gap-3 border-b border-gray-100 pb-3">
                <div>
                  <b>订单号 {order.id}</b>
                  <div className="text-sm text-gray-500">{order.createdAt}</div>
                </div>
                <span className="rounded bg-orange-50 px-3 py-2 text-sm font-bold text-brand-700">{order.status}</span>
              </div>
              <div className="space-y-2">
                {order.items.map((item) => (
                  <div key={`${order.id}-${item.productId}-${item.sku}`} className="flex justify-between text-sm">
                    <span>{item.title} · {item.sku} × {item.quantity}</span>
                    <b>{money(item.price * item.quantity)}</b>
                  </div>
                ))}
              </div>
              <div className="mt-4 flex flex-wrap items-center justify-between gap-3">
                <div className="text-sm text-gray-500">优惠 {money(order.discount)} · 实付 <b className="text-xl text-brand-600">{money(order.total)}</b></div>
                {order.status === "待付款" && (
                  <button onClick={() => pay(order.id)} className="focus-ring flex items-center gap-2 rounded bg-brand-500 px-5 py-3 font-bold text-white">
                    <CreditCard size={18} /> 模拟支付
                  </button>
                )}
              </div>
            </section>
          ))}
          {!items.length && <div className="rounded border border-gray-200 bg-white p-10 text-center text-gray-500">暂无订单。</div>}
        </div>
      </div>
    </AppShell>
  );
}
