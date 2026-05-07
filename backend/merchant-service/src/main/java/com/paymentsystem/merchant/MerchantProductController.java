package com.paymentsystem.merchant;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 商户商品接口控制器，提供商品维护、上下架和订单概览能力。
 */
@RestController
@RequestMapping("/api/merchant")
public class MerchantProductController {
    /** 商户商品自增 ID 生成器。 */
    private final AtomicLong ids = new AtomicLong(300);
    /** 以内存 Map 模拟商户商品存储。 */
    private final Map<Long, MerchantProduct> products = new ConcurrentHashMap<Long, MerchantProduct>();

    /**
     * 创建商户商品，初始为待审核状态。
     *
     * @param command 商品保存参数
     * @return 商户商品视图
     */
    @PostMapping("/products")
    public ApiResponse<MerchantProduct> create(@RequestBody ProductCommand command) {
        Long id = ids.incrementAndGet();
        MerchantProduct product = command.toProduct(id, false);
        products.put(id, product);
        return ApiResponse.ok(product);
    }

    /**
     * 更新商户商品基础信息，并保留已有上下架状态。
     *
     * @param id 商品 ID
     * @param command 商品保存参数
     * @return 商户商品视图
     */
    @PutMapping("/products/{id}")
    public ApiResponse<MerchantProduct> update(@PathVariable Long id, @RequestBody ProductCommand command) {
        MerchantProduct current = products.get(id);
        MerchantProduct product = command.toProduct(id, current != null && current.isOnSale());
        products.put(id, product);
        return ApiResponse.ok(product);
    }

    /**
     * 将商户商品上架。
     *
     * @param id 商品 ID
     * @return 商户商品视图
     */
    @PutMapping("/products/{id}/on-sale")
    public ApiResponse<MerchantProduct> onSale(@PathVariable Long id) {
        return changeSaleState(id, true);
    }

    /**
     * 将商户商品下架。
     *
     * @param id 商品 ID
     * @return 商户商品视图
     */
    @PutMapping("/products/{id}/off-sale")
    public ApiResponse<MerchantProduct> offSale(@PathVariable Long id) {
        return changeSaleState(id, false);
    }

    /**
     * 查询商户订单概览。
     *
     * @return 商户订单列表
     */
    @GetMapping("/orders")
    public ApiResponse<List<Map<String, Object>>> orders() {
        Map<String, Object> order = new HashMap<String, Object>();
        order.put("orderId", 20260001);
        order.put("status", "PAID");
        order.put("amount", "399.00");
        return ApiResponse.ok(Arrays.asList(order));
    }

    /**
     * 变更商品上下架状态。
     *
     * @param id 商品 ID
     * @param onSale 是否上架
     * @return 商户商品视图
     */
    private ApiResponse<MerchantProduct> changeSaleState(Long id, boolean onSale) {
        MerchantProduct current = products.get(id);
        if (current == null) {
            return ApiResponse.fail("PRODUCT_NOT_FOUND", "merchant product not found");
        }
        MerchantProduct next = current.withSaleState(onSale);
        products.put(id, next);
        return ApiResponse.ok(next);
    }
}
