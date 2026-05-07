"use client";

import Link from "next/link";
import { Heart, LucideIcon, MapPin, Package, UserRound } from "lucide-react";
import { useEffect, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { ProductCard } from "@/components/ProductCard";
import { products } from "@/lib/mock-data";
import { currentUser, favoriteIds, orders } from "@/lib/store";
import { Order, User } from "@/lib/types";

export function UserClient() {
  const [user, setUser] = useState<User | null>(null);
  const [myOrders, setMyOrders] = useState<Order[]>([]);
  const [favorites, setFavorites] = useState<number[]>([]);
  useEffect(() => {
    setUser(currentUser());
    setMyOrders(orders());
    setFavorites(favoriteIds());
  }, []);
  const menuItems: Array<[string, LucideIcon]> = [["我的订单", Package], ["我的收藏", Heart], ["收货地址", MapPin], ["个人资料", UserRound]];

  return (
    <AppShell>
      <div className="mx-auto max-w-7xl px-4 py-8">
        <section className="mb-6 rounded border border-gray-200 bg-white p-6 shadow-sm">
          <div className="flex flex-wrap items-center gap-4">
            <div className="grid h-16 w-16 place-items-center rounded bg-brand-50 text-brand-600"><UserRound size={30} /></div>
            <div>
              <h1 className="text-2xl font-black">{user?.name ?? "未登录用户"}</h1>
              <p className="text-sm text-gray-500">{user?.account ?? "请先登录以同步账户数据"}</p>
            </div>
            <Link href="/auth" className="ml-auto rounded bg-gray-100 px-4 py-2 font-bold">切换账号</Link>
          </div>
        </section>
        <div className="grid gap-5 lg:grid-cols-[260px_1fr]">
          <aside className="space-y-3 rounded border border-gray-200 bg-white p-4">
            {menuItems.map(([text, Icon]) => (
              <a key={text} href={`#${text}`} className="flex items-center gap-3 rounded px-3 py-3 hover:bg-orange-50">
                <Icon size={18} /> {text}
              </a>
            ))}
          </aside>
          <div className="space-y-5">
            <section id="我的订单" className="rounded border border-gray-200 bg-white p-5">
              <h2 className="mb-4 text-xl font-black">我的订单</h2>
              <div className="grid gap-3">
                {myOrders.slice(0, 3).map((order) => <Link key={order.id} href="/orders" className="flex justify-between rounded bg-gray-50 px-4 py-3"><span>{order.id}</span><b>{order.status}</b></Link>)}
                {!myOrders.length && <p className="text-gray-500">还没有订单。</p>}
              </div>
            </section>
            <section id="我的收藏" className="rounded border border-gray-200 bg-white p-5">
              <h2 className="mb-4 text-xl font-black">我的收藏</h2>
              <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                {products.filter((item) => favorites.includes(item.id)).map((product) => <ProductCard key={product.id} product={product} />)}
              </div>
              {!favorites.length && <p className="text-gray-500">暂无收藏商品。</p>}
            </section>
            <section id="收货地址" className="rounded border border-gray-200 bg-white p-5">
              <h2 className="mb-4 text-xl font-black">收货地址管理</h2>
              <div className="rounded bg-gray-50 p-4">上海市浦东新区优选路 88 号 18F 陈小优 13800138000</div>
            </section>
            <section id="个人资料" className="rounded border border-gray-200 bg-white p-5">
              <h2 className="mb-4 text-xl font-black">个人资料</h2>
              <div className="grid gap-3 md:grid-cols-2">
                <input defaultValue={user?.name ?? "优选会员"} className="focus-ring rounded border border-gray-200 px-3 py-2" />
                <input defaultValue={user?.account ?? "user@example.com"} className="focus-ring rounded border border-gray-200 px-3 py-2" />
              </div>
            </section>
          </div>
        </div>
      </div>
    </AppShell>
  );
}
