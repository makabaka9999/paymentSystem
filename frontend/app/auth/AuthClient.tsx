"use client";

import { useRouter } from "next/navigation";
import { FormEvent, useState } from "react";
import { AppShell } from "@/components/AppShell";
import { login } from "@/lib/store";
import { Role } from "@/lib/types";

export function AuthClient() {
  const router = useRouter();
  const [mode, setMode] = useState<"login" | "register">("login");
  const [role, setRole] = useState<Role>("USER");
  const [error, setError] = useState("");

  function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const account = String(data.get("account") || "");
    const password = String(data.get("password") || "");
    const confirm = String(data.get("confirm") || "");
    if (!/^\S+@\S+\.\S+$|^1\d{10}$/.test(account)) {
      setError("请输入正确的邮箱或手机号");
      return;
    }
    if (password.length < 6) {
      setError("密码至少 6 位");
      return;
    }
    if (mode === "register" && password !== confirm) {
      setError("两次密码不一致");
      return;
    }
    login(account, password, role);
    router.push(role === "MERCHANT" ? "/merchant" : role === "ADMIN" ? "/admin" : "/user");
  }

  return (
    <AppShell>
      <div className="mx-auto grid min-h-[640px] max-w-7xl items-center px-4 py-8 lg:grid-cols-[1fr_420px]">
        <section className="hidden pr-12 lg:block">
          <div className="text-sm font-bold text-brand-600">欢迎来到优选商城</div>
          <h1 className="mt-3 text-5xl font-black leading-tight">一个清爽、可信、好逛的多商家购物平台。</h1>
          <p className="mt-5 text-lg text-gray-600">MVP 支持消费者购物、商家发布商品、平台后台管理和完整模拟支付链路。</p>
        </section>
        <form onSubmit={submit} className="rounded border border-gray-200 bg-white p-6 shadow-soft">
          <div className="mb-5 grid grid-cols-2 rounded bg-gray-100 p-1">
            <button type="button" onClick={() => setMode("login")} className={`rounded py-2 font-bold ${mode === "login" ? "bg-white text-brand-600 shadow-sm" : ""}`}>登录</button>
            <button type="button" onClick={() => setMode("register")} className={`rounded py-2 font-bold ${mode === "register" ? "bg-white text-brand-600 shadow-sm" : ""}`}>注册</button>
          </div>
          <label className="mb-3 block">
            <span className="mb-1 block text-sm font-semibold">手机号或邮箱</span>
            <input name="account" placeholder="user@example.com / 13800138000" className="focus-ring w-full rounded border border-gray-200 px-3 py-3" />
          </label>
          <label className="mb-3 block">
            <span className="mb-1 block text-sm font-semibold">密码</span>
            <input name="password" type="password" placeholder="至少 6 位" className="focus-ring w-full rounded border border-gray-200 px-3 py-3" />
          </label>
          {mode === "register" && (
            <label className="mb-3 block">
              <span className="mb-1 block text-sm font-semibold">确认密码</span>
              <input name="confirm" type="password" className="focus-ring w-full rounded border border-gray-200 px-3 py-3" />
            </label>
          )}
          <div className="mb-4 grid grid-cols-3 gap-2">
            {(["USER", "MERCHANT", "ADMIN"] as Role[]).map((item) => (
              <button type="button" key={item} onClick={() => setRole(item)} className={`rounded border px-3 py-2 text-sm font-bold ${role === item ? "border-brand-500 bg-orange-50 text-brand-700" : "border-gray-200"}`}>
                {item === "USER" ? "消费者" : item === "MERCHANT" ? "商家" : "管理员"}
              </button>
            ))}
          </div>
          {error && <div className="mb-3 rounded bg-red-50 px-3 py-2 text-sm text-red-600">{error}</div>}
          <button className="focus-ring w-full rounded bg-brand-500 px-4 py-3 font-bold text-white">{mode === "login" ? "登录" : "注册账号"}</button>
          <div className="mt-4 text-sm text-gray-500">测试密码可使用：`password`</div>
        </form>
      </div>
    </AppShell>
  );
}
