package com.paymentsystem.product;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 商品接口控制器，提供商品列表和商品详情查询能力。
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    /** 以内存 Map 模拟商品库。 */
    private final Map<Long, ProductView> products = new ConcurrentHashMap<Long, ProductView>();

    /**
     * 初始化演示商品数据。
     */
    public ProductController() {
        products.put(101L, new ProductView(101L, 10001L, "机械键盘 Pro", "办公外设", new BigDecimal("399.00"), 300, true, "适合高频输入的热插拔键盘"));
        products.put(102L, new ProductView(102L, 10002L, "无线降噪耳机", "数码音频", new BigDecimal("699.00"), 160, true, "通勤和办公两用"));
        products.put(103L, new ProductView(103L, 10003L, "人体工学椅", "办公家具", new BigDecimal("1299.00"), 48, true, "长时间工作支撑"));
    }

    /**
     * 查询在售商品列表，可按关键字过滤商品名称或类目。
     *
     * @param keyword 查询关键字
     * @return 商品列表
     */
    @GetMapping
    public ApiResponse<List<ProductView>> list(@RequestParam(required = false) String keyword) {
        String filter = keyword == null ? "" : keyword.trim();
        List<ProductView> result = new ArrayList<ProductView>();
        for (ProductView item : products.values()) {
            if (item.isOnSale() && (filter.isEmpty() || item.getName().contains(filter) || item.getCategory().contains(filter))) {
                result.add(item);
            }
        }
        return ApiResponse.ok(result);
    }

    /**
     * 查询指定在售商品详情。
     *
     * @param id 商品 ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ProductView> detail(@PathVariable Long id) {
        ProductView product = products.get(id);
        if (product == null || !product.isOnSale()) {
            return ApiResponse.fail("PRODUCT_NOT_FOUND", "product is not available");
        }
        return ApiResponse.ok(product);
    }
}
