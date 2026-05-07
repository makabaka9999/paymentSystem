import { AppShell } from "@/components/AppShell";
import { ProductCard } from "@/components/ProductCard";
import { categories, products } from "@/lib/mock-data";

export default function ProductsPage({ searchParams }: { searchParams: { q?: string; category?: string; sort?: string; min?: string; max?: string; shop?: string } }) {
  const q = searchParams.q?.trim() ?? "";
  const category = searchParams.category ?? "";
  const min = Number(searchParams.min || 0);
  const max = Number(searchParams.max || Number.MAX_SAFE_INTEGER);
  const shopId = searchParams.shop ? Number(searchParams.shop) : null;
  const sort = searchParams.sort ?? "sales";
  const filtered = products
    .filter((item) => (!q || item.title.includes(q) || item.category.includes(q)))
    .filter((item) => (!category || item.category === category))
    .filter((item) => item.price >= min && item.price <= max)
    .filter((item) => (!shopId || item.shopId === shopId))
    .sort((a, b) => (sort === "rating" ? b.rating - a.rating : b.sales - a.sales));

  return (
    <AppShell>
      <div className="mx-auto max-w-7xl px-4 py-8">
        <div className="mb-5 rounded border border-gray-200 bg-white p-4">
          <div className="mb-4 flex flex-wrap gap-2">
            <a className={`rounded px-3 py-2 text-sm ${!category ? "bg-gray-900 text-white" : "bg-gray-100"}`} href="/products">全部分类</a>
            {categories.map((item) => (
              <a key={item} className={`rounded px-3 py-2 text-sm ${category === item ? "bg-gray-900 text-white" : "bg-gray-100"}`} href={`/products?category=${encodeURIComponent(item)}`}>
                {item}
              </a>
            ))}
          </div>
          <form className="grid gap-3 md:grid-cols-[1fr_120px_120px_120px]">
            <input name="q" defaultValue={q} placeholder="搜索结果关键词" className="focus-ring rounded border border-gray-200 px-3 py-2" />
            <input name="min" defaultValue={searchParams.min} placeholder="最低价" className="focus-ring rounded border border-gray-200 px-3 py-2" />
            <input name="max" defaultValue={searchParams.max} placeholder="最高价" className="focus-ring rounded border border-gray-200 px-3 py-2" />
            <button className="focus-ring rounded bg-brand-500 px-4 py-2 font-bold text-white">筛选</button>
          </form>
          <div className="mt-3 flex gap-2 text-sm">
            <a href={`/products?q=${encodeURIComponent(q)}&category=${encodeURIComponent(category)}&sort=sales`} className={`rounded px-3 py-2 ${sort === "sales" ? "bg-orange-50 text-brand-700" : "text-gray-600"}`}>销量排序</a>
            <a href={`/products?q=${encodeURIComponent(q)}&category=${encodeURIComponent(category)}&sort=rating`} className={`rounded px-3 py-2 ${sort === "rating" ? "bg-orange-50 text-brand-700" : "text-gray-600"}`}>评分排序</a>
          </div>
        </div>
        <div className="mb-4 text-sm text-gray-500">找到 {filtered.length} 个商品</div>
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {filtered.map((product) => <ProductCard key={product.id} product={product} />)}
        </div>
      </div>
    </AppShell>
  );
}
