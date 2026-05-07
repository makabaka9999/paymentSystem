import Link from "next/link";
import { Clock, Flame, LucideIcon, ShieldCheck, Sparkles, Store } from "lucide-react";
import { AppShell } from "@/components/AppShell";
import { ProductCard } from "@/components/ProductCard";
import { SectionTitle } from "@/components/SectionTitle";
import { categories, products, shops } from "@/lib/mock-data";
import { money } from "@/lib/format";

export default function HomePage() {
  const recommended = products.slice(0, 4);
  const deals = products.filter((item) => item.originPrice).slice(0, 3);
  const trustCards: Array<[string, string, LucideIcon]> = [
    ["正品保障", "商家资质与商品审核", ShieldCheck],
    ["限时优惠", "优惠券与活动价叠加", Clock],
    ["开店工具", "发布、订单、店铺一站管理", Store],
  ];

  return (
    <AppShell>
      <section className="bg-white">
        <div className="mx-auto grid max-w-7xl gap-5 px-4 py-6 lg:grid-cols-[220px_1fr_280px]">
          <aside className="hidden rounded border border-gray-200 bg-mist p-3 lg:block">
            {categories.map((item) => (
              <Link key={item} href={`/products?category=${encodeURIComponent(item)}`} className="flex items-center justify-between rounded px-3 py-2 text-sm hover:bg-white hover:text-brand-600">
                {item}
                <span className="text-gray-400">›</span>
              </Link>
            ))}
          </aside>
          <div className="relative min-h-[340px] overflow-hidden rounded bg-gray-950 p-8 text-white">
            <div className="absolute inset-0 opacity-80" style={{ background: "radial-gradient(circle at 80% 20%, #fb923c 0, transparent 30%), linear-gradient(135deg,#111827,#ea580c)" }} />
            <div className="relative z-10 max-w-xl">
              <div className="mb-4 inline-flex items-center gap-2 rounded bg-white/15 px-3 py-2 text-sm">
                <Sparkles size={16} /> 五月优选焕新季
              </div>
              <h1 className="text-4xl font-black leading-tight md:text-5xl">优选商城</h1>
              <p className="mt-4 text-lg text-orange-50">从好店、好价到顺手的购物流程，为消费者和商家搭一个干净好用的多商家平台。</p>
              <div className="mt-7 flex flex-wrap gap-3">
                <Link href="/products" className="focus-ring rounded bg-white px-5 py-3 font-bold text-brand-700">
                  逛精选商品
                </Link>
                <Link href="/merchant" className="focus-ring rounded border border-white/50 px-5 py-3 font-bold text-white">
                  商家入驻
                </Link>
              </div>
            </div>
          </div>
          <aside className="grid gap-3">
            {trustCards.map(([title, text, Icon]) => (
              <div key={title} className="rounded border border-gray-200 bg-white p-4 shadow-sm">
                <Icon className="mb-3 text-brand-600" size={24} />
                <div className="font-bold">{title}</div>
                <p className="mt-1 text-sm text-gray-500">{text}</p>
              </div>
            ))}
          </aside>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-8">
        <SectionTitle title="推荐商品" action={<Link className="text-sm font-semibold text-brand-600" href="/products">查看全部</Link>} />
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {recommended.map((product) => <ProductCard key={product.id} product={product} />)}
        </div>
      </section>

      <section className="bg-white py-8">
        <div className="mx-auto max-w-7xl px-4">
          <SectionTitle title="热门店铺" />
          <div className="grid gap-4 md:grid-cols-4">
            {shops.map((shop) => (
              <Link key={shop.id} href={`/products?shop=${shop.id}`} className="rounded border border-gray-200 p-4 transition hover:border-brand-500 hover:shadow-soft">
                <div className="mb-4 grid h-16 w-16 place-items-center rounded bg-brand-50 text-xl font-black text-brand-600">{shop.name.slice(0, 1)}</div>
                <div className="font-bold text-gray-900">{shop.name}</div>
                <p className="mt-1 min-h-10 text-sm text-gray-500">{shop.slogan}</p>
                <div className="mt-3 text-sm text-gray-600">评分 {shop.score} · 粉丝 {shop.fans}</div>
              </Link>
            ))}
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-8">
        <SectionTitle title="限时优惠" />
        <div className="grid gap-4 md:grid-cols-3">
          {deals.map((product) => (
            <Link key={product.id} href={`/products/${product.id}`} className="flex gap-4 rounded border border-orange-100 bg-white p-4 shadow-sm">
              <div className="grid h-24 w-24 shrink-0 place-items-center rounded text-sm font-bold text-white" style={{ background: product.image }}>
                <Flame size={24} />
              </div>
              <div>
                <div className="font-bold">{product.title}</div>
                <div className="mt-2 text-2xl font-black text-brand-600">{money(product.price)}</div>
                <div className="text-sm text-gray-400 line-through">{money(product.originPrice ?? product.price)}</div>
              </div>
            </Link>
          ))}
        </div>
      </section>
    </AppShell>
  );
}
