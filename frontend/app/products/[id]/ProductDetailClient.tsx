"use client";

import Link from "next/link";
import { Heart, Minus, Plus, ShoppingCart, Star } from "lucide-react";
import { useEffect, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { money } from "@/lib/format";
import { reviews, shops } from "@/lib/mock-data";
import { addToCart, favoriteIds, toggleFavorite } from "@/lib/store";
import { Product } from "@/lib/types";

export function ProductDetailClient({ product }: { product: Product }) {
  const [sku, setSku] = useState(product.skus[0]);
  const [quantity, setQuantity] = useState(1);
  const [favorited, setFavorited] = useState(false);
  const [notice, setNotice] = useState("");
  const shop = shops.find((item) => item.id === product.shopId);
  const productReviews = reviews.filter((item) => item.productId === product.id);

  useEffect(() => setFavorited(favoriteIds().includes(product.id)), [product.id]);

  function add() {
    addToCart(product.id, sku, quantity);
    setNotice("已加入购物车");
  }

  return (
    <AppShell>
      <div className="mx-auto max-w-7xl px-4 py-8">
        <div className="grid gap-6 lg:grid-cols-[520px_1fr]">
          <div className="grid aspect-square place-items-center rounded bg-white p-8 text-3xl font-black text-white shadow-sm" style={{ background: product.image }}>
            {product.category}
          </div>
          <section className="rounded border border-gray-200 bg-white p-6 shadow-sm">
            <div className="mb-3 text-sm text-gray-500">{shop?.name}</div>
            <h1 className="text-3xl font-black leading-tight text-gray-950">{product.title}</h1>
            <p className="mt-3 text-gray-600">{product.description}</p>
            <div className="mt-5 rounded bg-orange-50 p-4">
              <span className="text-sm text-brand-700">活动价</span>
              <span className="ml-3 text-3xl font-black text-brand-600">{money(product.price)}</span>
              {product.originPrice && <span className="ml-3 text-gray-400 line-through">{money(product.originPrice)}</span>}
            </div>
            <div className="mt-4 grid gap-3 text-sm text-gray-600 sm:grid-cols-3">
              <div>销量 <b className="text-gray-900">{product.sales}</b></div>
              <div className="flex items-center gap-1">评分 <Star size={16} className="text-amber-500" fill="currentColor" /> <b>{product.rating}</b></div>
              <div>库存 <b className="text-gray-900">{product.stock}</b></div>
            </div>
            <div className="mt-6">
              <div className="mb-2 font-bold">规格</div>
              <div className="flex flex-wrap gap-2">
                {product.skus.map((item) => (
                  <button key={item} onClick={() => setSku(item)} className={`focus-ring rounded border px-4 py-2 ${sku === item ? "border-brand-500 bg-orange-50 text-brand-700" : "border-gray-200"}`}>
                    {item}
                  </button>
                ))}
              </div>
            </div>
            <div className="mt-6">
              <div className="mb-2 font-bold">数量</div>
              <div className="inline-flex items-center rounded border border-gray-200">
                <button className="p-3" onClick={() => setQuantity(Math.max(1, quantity - 1))}><Minus size={16} /></button>
                <span className="w-12 text-center">{quantity}</span>
                <button className="p-3" onClick={() => setQuantity(Math.min(product.stock, quantity + 1))}><Plus size={16} /></button>
              </div>
            </div>
            <div className="mt-7 flex flex-wrap gap-3">
              <button onClick={add} className="focus-ring flex items-center gap-2 rounded border border-brand-500 px-6 py-3 font-bold text-brand-700">
                <ShoppingCart size={18} /> 加入购物车
              </button>
              <Link href="/checkout" onClick={add} className="focus-ring rounded bg-brand-500 px-6 py-3 font-bold text-white">
                立即购买
              </Link>
              <button
                onClick={() => setFavorited(toggleFavorite(product.id).includes(product.id))}
                className={`focus-ring flex items-center gap-2 rounded px-5 py-3 font-bold ${favorited ? "bg-red-50 text-red-600" : "bg-gray-100 text-gray-700"}`}
              >
                <Heart size={18} fill={favorited ? "currentColor" : "none"} /> 收藏
              </button>
            </div>
            {notice && <div className="mt-3 text-sm font-semibold text-brand-700">{notice}</div>}
          </section>
        </div>
        <section className="mt-8 grid gap-6 lg:grid-cols-[1fr_360px]">
          <div className="rounded border border-gray-200 bg-white p-6">
            <h2 className="mb-4 text-xl font-black">商品详情介绍</h2>
            <p className="leading-8 text-gray-700">{product.description} 商品信息为 MVP 占位数据，可后续接入图文详情、参数表、售后承诺和商家资质。</p>
          </div>
          <div className="rounded border border-gray-200 bg-white p-6">
            <h2 className="mb-4 text-xl font-black">用户评价</h2>
            <div className="space-y-4">
              {(productReviews.length ? productReviews : reviews.slice(0, 2)).map((review) => (
                <div key={review.id} className="border-b border-gray-100 pb-4 last:border-0">
                  <div className="flex items-center justify-between text-sm">
                    <b>{review.user}</b>
                    <span className="text-amber-500">{review.rating} 分</span>
                  </div>
                  <p className="mt-2 text-sm text-gray-600">{review.content}</p>
                </div>
              ))}
            </div>
          </div>
        </section>
      </div>
    </AppShell>
  );
}
