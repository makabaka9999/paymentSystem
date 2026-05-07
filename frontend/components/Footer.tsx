export function Footer() {
  return (
    <footer className="mt-12 border-t border-gray-200 bg-white">
      <div className="mx-auto grid max-w-7xl gap-6 px-4 py-8 text-sm text-gray-600 md:grid-cols-4">
        <div>
          <div className="mb-2 font-bold text-gray-900">优选商城</div>
          <p>原创综合电商 MVP，面向消费者、商家和平台运营人员。</p>
        </div>
        <div>
          <div className="mb-2 font-bold text-gray-900">消费者服务</div>
          <p>订单查询、收藏、优惠券、评价、模拟支付。</p>
        </div>
        <div>
          <div className="mb-2 font-bold text-gray-900">商家服务</div>
          <p>商品发布、库存管理、订单处理、店铺信息维护。</p>
        </div>
        <div>
          <div className="mb-2 font-bold text-gray-900">平台治理</div>
          <p>用户管理、商家审核、商品审核、交易数据统计。</p>
        </div>
      </div>
    </footer>
  );
}
