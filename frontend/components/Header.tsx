"use client";

import Link from "next/link";
import { useRouter, useSearchParams } from "next/navigation";
import { Heart, Menu, Search, ShoppingCart, Store, UserRound } from "lucide-react";
import { FormEvent, useEffect, useState } from "react";
import { categories } from "@/lib/mock-data";
import { currentUser } from "@/lib/store";
import { User } from "@/lib/types";

export function Header() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [keyword, setKeyword] = useState(searchParams.get("q") ?? "");
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => setUser(currentUser()), []);

  function onSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    router.push(`/products?q=${encodeURIComponent(keyword.trim())}`);
  }

  return (
    <header className="sticky top-0 z-30 border-b border-orange-100 bg-white/95 backdrop-blur">
      <div className="mx-auto flex max-w-7xl items-center gap-4 px-4 py-3">
        <Link href="/" className="flex items-center gap-2 text-xl font-black text-brand-600">
          <span className="grid h-9 w-9 place-items-center rounded bg-brand-500 text-white">优</span>
          优选商城
        </Link>
        <form onSubmit={onSearch} className="hidden flex-1 items-center rounded border border-brand-500 bg-white md:flex">
          <input
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
            placeholder="搜索商品、店铺或灵感"
            className="focus-ring h-11 flex-1 rounded-l px-4"
          />
          <button className="focus-ring flex h-11 items-center gap-2 rounded-r bg-brand-500 px-5 font-semibold text-white">
            <Search size={18} /> 搜索
          </button>
        </form>
        <nav className="ml-auto flex items-center gap-1 text-sm text-gray-700">
          <Link className="focus-ring rounded px-3 py-2 hover:bg-orange-50" href="/cart" title="购物车">
            <ShoppingCart size={20} />
          </Link>
          <Link className="focus-ring rounded px-3 py-2 hover:bg-orange-50" href="/user" title="用户中心">
            <UserRound size={20} />
          </Link>
          <Link className="hidden rounded px-3 py-2 hover:bg-orange-50 sm:block" href="/merchant">
            商家中心
          </Link>
          <Link className="hidden rounded px-3 py-2 hover:bg-orange-50 sm:block" href="/admin">
            后台
          </Link>
          <Link className="rounded bg-gray-100 px-3 py-2 font-medium hover:bg-orange-50" href="/auth">
            {user ? user.name : "登录"}
          </Link>
        </nav>
      </div>
      <div className="mx-auto flex max-w-7xl items-center gap-2 overflow-x-auto px-4 pb-3 text-sm">
        <span className="flex shrink-0 items-center gap-1 rounded bg-gray-900 px-3 py-2 font-semibold text-white">
          <Menu size={16} /> 商品分类
        </span>
        {categories.map((item) => (
          <Link key={item} className="shrink-0 rounded px-3 py-2 hover:bg-orange-50" href={`/products?category=${encodeURIComponent(item)}`}>
            {item}
          </Link>
        ))}
        <Link className="ml-auto hidden shrink-0 items-center gap-1 rounded px-3 py-2 text-brand-700 hover:bg-orange-50 md:flex" href="/user">
          <Heart size={16} /> 我的收藏
        </Link>
        <Link className="hidden shrink-0 items-center gap-1 rounded px-3 py-2 text-brand-700 hover:bg-orange-50 md:flex" href="/merchant">
          <Store size={16} /> 免费开店
        </Link>
      </div>
    </header>
  );
}
