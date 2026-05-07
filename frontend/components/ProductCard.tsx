import Link from "next/link";
import { Heart, Star } from "lucide-react";
import { money } from "@/lib/format";
import { shops } from "@/lib/mock-data";
import { Product } from "@/lib/types";

export function ProductCard({ product }: { product: Product }) {
  const shop = shops.find((item) => item.id === product.shopId);

  return (
    <Link href={`/products/${product.id}`} className="group block overflow-hidden rounded border border-gray-200 bg-white shadow-sm transition hover:-translate-y-1 hover:shadow-soft">
      <div className="grid aspect-[4/3] place-items-center p-6 text-center text-lg font-bold text-white" style={{ background: product.image }}>
        {product.category}
      </div>
      <div className="space-y-3 p-4">
        <div className="line-clamp-2 min-h-12 font-semibold text-gray-900 group-hover:text-brand-600">{product.title}</div>
        <div className="flex flex-wrap gap-2">
          {product.tags.slice(0, 2).map((tag) => (
            <span key={tag} className="rounded bg-orange-50 px-2 py-1 text-xs text-brand-700">
              {tag}
            </span>
          ))}
        </div>
        <div className="flex items-end justify-between">
          <div>
            <div className="text-xl font-black text-brand-600">{money(product.price)}</div>
            <div className="text-xs text-gray-500">{shop?.name}</div>
          </div>
          <div className="text-right text-xs text-gray-500">
            <div className="flex items-center gap-1 text-amber-500">
              <Star size={14} fill="currentColor" /> {product.rating}
            </div>
            <div>已售 {product.sales}</div>
          </div>
        </div>
      </div>
    </Link>
  );
}
