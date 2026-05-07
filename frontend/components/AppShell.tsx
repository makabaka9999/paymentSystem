import { Suspense } from "react";
import { Footer } from "./Footer";
import { Header } from "./Header";

export function AppShell({ children }: { children: React.ReactNode }) {
  return (
    <>
      <Suspense>
        <Header />
      </Suspense>
      <main>{children}</main>
      <Footer />
    </>
  );
}
