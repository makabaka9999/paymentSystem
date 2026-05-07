package com.paymentsystem.inventory.controller;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.inventory.dto.Stock;
import com.paymentsystem.inventory.dto.StockCommand;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 库存接口控制器，提供库存查询、冻结、扣减和释放能力。
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    /** 以内存 Map 模拟 SKU 库存。 */
    private final Map<Long, Stock> stocks = new ConcurrentHashMap<Long, Stock>();

    /**
     * 初始化演示库存。
     */
    public InventoryController() {
        stocks.put(10001L, new Stock(10001L, 300, 0));
        stocks.put(10002L, new Stock(10002L, 160, 0));
        stocks.put(10003L, new Stock(10003L, 48, 0));
    }

    /**
     * 查询指定 SKU 的库存。
     *
     * @param skuId 商品 SKU ID
     * @return 库存视图
     */
    @GetMapping("/{skuId}")
    public ApiResponse<Stock> get(@PathVariable Long skuId) {
        Stock stock = stocks.containsKey(skuId) ? stocks.get(skuId) : new Stock(skuId, 0, 0);
        return ApiResponse.ok(stock);
    }

    /**
     * 冻结指定数量库存。
     *
     * @param command 库存操作参数
     * @return 冻结后的库存视图
     */
    @PostMapping("/freeze")
    public ApiResponse<Stock> freeze(@RequestBody StockCommand command) {
        Stock value = getOrEmpty(command.getSkuId());
        if (value.getAvailable() < command.getQuantity()) {
            return ApiResponse.fail("INSUFFICIENT_STOCK", "insufficient stock");
        }
        Stock next = new Stock(command.getSkuId(), value.getAvailable() - command.getQuantity(), value.getFrozen() + command.getQuantity());
        stocks.put(command.getSkuId(), next);
        return ApiResponse.ok(next);
    }

    /**
     * 提交冻结库存，减少冻结数量。
     *
     * @param command 库存操作参数
     * @return 扣减后的库存视图
     */
    @PostMapping("/commit")
    public ApiResponse<Stock> commit(@RequestBody StockCommand command) {
        Stock value = getOrEmpty(command.getSkuId());
        Stock next = new Stock(command.getSkuId(), value.getAvailable(), Math.max(0, value.getFrozen() - command.getQuantity()));
        stocks.put(command.getSkuId(), next);
        return ApiResponse.ok(next);
    }

    /**
     * 释放冻结库存并归还可用库存。
     *
     * @param command 库存操作参数
     * @return 释放后的库存视图
     */
    @PostMapping("/release")
    public ApiResponse<Stock> release(@RequestBody StockCommand command) {
        Stock value = getOrEmpty(command.getSkuId());
        int release = Math.min(value.getFrozen(), command.getQuantity());
        Stock next = new Stock(command.getSkuId(), value.getAvailable() + release, value.getFrozen() - release);
        stocks.put(command.getSkuId(), next);
        return ApiResponse.ok(next);
    }

    /**
     * 获取 SKU 库存，不存在时返回空库存。
     *
     * @param skuId 商品 SKU ID
     * @return 库存视图
     */
    private Stock getOrEmpty(Long skuId) {
        Stock value = stocks.get(skuId);
        return value == null ? new Stock(skuId, 0, 0) : value;
    }
}
