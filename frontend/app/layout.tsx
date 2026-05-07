import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "优选商城 - 多商家电商平台",
  description: "原创中文多商家电商平台 MVP",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="zh-CN">
      <body>{children}</body>
    </html>
  );
}
