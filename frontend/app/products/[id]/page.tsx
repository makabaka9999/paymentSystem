import { notFound } from "next/navigation";
import { products } from "@/lib/mock-data";
import { ProductDetailClient } from "./ProductDetailClient";

export default function ProductDetailPage({ params }: { params: { id: string } }) {
  const product = products.find((item) => item.id === Number(params.id));
  if (!product) notFound();
  return <ProductDetailClient product={product} />;
}
