"use client";

import Link from "next/link";
import { Trash2 } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { money } from "@/lib/format";
import { products } from "@/lib/mock-data";
import { cartItems, updateCart } from "@/lib/store";
import { CartItem } from "@/lib/types";

export function CartClient() {
  const [items, setItems] = useState<CartItem[]>([]);
  useEffect(() => setItems(cartItems()), []);

  const rows = items.map((item) => ({ ...item, product: products.find((product) => product.id === item.productId)! })).filter((item) => item.product);
  const total = useMemo(() => rows.filter((item) => item.selected).reduce((sum, item) => sum + item.product.price * item.quantity, 0), [rows]);

  function commit(next: CartItem[]) {
    setItems(next);
    updateCart(next);
  }

  return (
    <AppShell>
      <div className="mx-auto max-w-7xl px-4 py-8">
        <h1 className="mb-5 text-3xl font-black">购物车</h1>
        <div className="grid gap-5 lg:grid-cols-[1fr_320px]">
          <div className="space-y-3">
            {rows.map((item) => (
              <div key={`${item.productId}-${item.sku}`} className="grid gap-4 rounded border border-gray-200 bg-white p-4 md:grid-cols-[32px_96px_1fr_150px_120px_48px] md:items-center">
                <input
                  type="checkbox"
                  checked={item.selected}
                  onChange={(event) => commit(items.map((cart) => (cart.productId === item.productId && cart.sku === item.sku ? { ...cart, selected: event.target.checked } : cart)))}
                />
                <div className="grid h-24 place-items-center rounded text-xs font-bold text-white" style={{ background: item.product.image }}>{item.product.category}</div>
                <div>
                  <Link href={`/products/${item.product.id}`} className="font-bold hover:text-brand-600">{item.product.title}</Link>
                  <div className="mt-1 text-sm text-gray-500">{item.sku}</div>
                </div>
                <div className="font-black text-brand-600">{money(item.product.price)}</div>
                <input
                  type="number"
                  min={1}
                  value={item.quantity}
                  onChange={(event) => commit(items.map((cart) => (cart.productId === item.productId && cart.sku === item.sku ? { ...cart, quantity: Number(event.target.value) } : cart)))}
                  className="focus-ring w-24 rounded border border-gray-200 px-3 py-2"
                />
                <button onClick={() => commit(items.filter((cart) => !(cart.productId === item.productId && cart.sku === item.sku)))} className="focus-ring rounded p-3 text-gray-500 hover:bg-red-50 hover:text-red-600">
                  <Trash2 size={18} />
                </button>
              </div>
            ))}
            {!rows.length && <div className="rounded border border-gray-200 bg-white p-10 text-center text-gray-500">购物车还是空的，去首页挑点好东西吧。</div>}
          </div>
          <aside className="h-fit rounded border border-gray-200 bg-white p-5 shadow-sm">
            <div className="flex justify-between text-gray-600"><span>已选商品</span><b>{rows.filter((item) => item.selected).length} 件</b></div>
            <div className="mt-4 flex justify-between text-lg"><span>合计</span><b className="text-2xl text-brand-600">{money(total)}</b></div>
            <Link href="/checkout" className="focus-ring mt-5 block rounded bg-brand-500 px-5 py-3 text-center font-bold text-white">去结算</Link>
          </aside>
        </div>
      </div>
    </AppShell>
  );
}
