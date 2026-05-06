package com.paymentsystem.inventory;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final Map<Long, Stock> stocks = new ConcurrentHashMap<Long, Stock>();

    public InventoryController() {
        stocks.put(10001L, new Stock(10001L, 300, 0));
        stocks.put(10002L, new Stock(10002L, 160, 0));
        stocks.put(10003L, new Stock(10003L, 48, 0));
    }

    @GetMapping("/{skuId}")
    public ApiResponse<Stock> get(@PathVariable Long skuId) {
        Stock stock = stocks.containsKey(skuId) ? stocks.get(skuId) : new Stock(skuId, 0, 0);
        return ApiResponse.ok(stock);
    }

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

    @PostMapping("/commit")
    public ApiResponse<Stock> commit(@RequestBody StockCommand command) {
        Stock value = getOrEmpty(command.getSkuId());
        Stock next = new Stock(command.getSkuId(), value.getAvailable(), Math.max(0, value.getFrozen() - command.getQuantity()));
        stocks.put(command.getSkuId(), next);
        return ApiResponse.ok(next);
    }

    @PostMapping("/release")
    public ApiResponse<Stock> release(@RequestBody StockCommand command) {
        Stock value = getOrEmpty(command.getSkuId());
        int release = Math.min(value.getFrozen(), command.getQuantity());
        Stock next = new Stock(command.getSkuId(), value.getAvailable() + release, value.getFrozen() - release);
        stocks.put(command.getSkuId(), next);
        return ApiResponse.ok(next);
    }

    private Stock getOrEmpty(Long skuId) {
        Stock value = stocks.get(skuId);
        return value == null ? new Stock(skuId, 0, 0) : value;
    }

    public static class Stock {
        private Long skuId;
        private Integer available;
        private Integer frozen;
        public Stock(Long skuId, Integer available, Integer frozen) { this.skuId = skuId; this.available = available; this.frozen = frozen; }
        public Long getSkuId() { return skuId; }
        public Integer getAvailable() { return available; }
        public Integer getFrozen() { return frozen; }
    }
    public static class StockCommand {
        private Long skuId;
        private Integer quantity;
        private String bizNo;
        public Long getSkuId() { return skuId; }
        public void setSkuId(Long skuId) { this.skuId = skuId; }
        public Integer getQuantity() { return quantity == null ? 0 : quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getBizNo() { return bizNo; }
        public void setBizNo(String bizNo) { this.bizNo = bizNo; }
    }
}
