package com.paymentsystem.cart;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 购物车接口控制器，负责查询、添加、更新和删除当前用户购物车商品。
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {
    /** 按用户 ID 隔离的内存购物车数据。 */
    private final Map<Long, Map<Long, CartItem>> carts = new ConcurrentHashMap<Long, Map<Long, CartItem>>();

    /**
     * 查询当前用户购物车。
     *
     * @param request HTTP 请求对象
     * @return 当前用户购物车明细
     */
    @GetMapping
    public ApiResponse<ArrayList<CartItem>> get(HttpServletRequest request) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.get(userId);
        return ApiResponse.ok(new ArrayList<CartItem>(userCart == null ? new ConcurrentHashMap<Long, CartItem>().values() : userCart.values()));
    }

    /**
     * 添加商品到当前用户购物车，已存在时累加数量。
     *
     * @param request HTTP 请求对象
     * @param command 购物车操作参数
     * @return 更新后的购物车明细
     */
    @PostMapping("/items")
    public ApiResponse<ArrayList<CartItem>> add(HttpServletRequest request, @RequestBody CartCommand command) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.computeIfAbsent(userId, ignored -> new ConcurrentHashMap<Long, CartItem>());
        CartItem existing = userCart.get(command.getSkuId());
        userCart.put(command.getSkuId(), existing == null ? command.toItem() : existing.withQuantity(existing.getQuantity() + command.getQuantity()));
        return get(request);
    }

    /**
     * 更新指定 SKU 的购买数量。
     *
     * @param request HTTP 请求对象
     * @param skuId 商品 SKU ID
     * @param command 购物车操作参数
     * @return 更新后的购物车明细
     */
    @PutMapping("/items/{skuId}")
    public ApiResponse<ArrayList<CartItem>> update(HttpServletRequest request, @PathVariable Long skuId, @RequestBody CartCommand command) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.computeIfAbsent(userId, ignored -> new ConcurrentHashMap<Long, CartItem>());
        CartItem existing = userCart.get(skuId);
        if (existing != null) {
            userCart.put(skuId, existing.withQuantity(command.getQuantity()));
        }
        return get(request);
    }

    /**
     * 从当前用户购物车删除指定 SKU。
     *
     * @param request HTTP 请求对象
     * @param skuId 商品 SKU ID
     * @return 更新后的购物车明细
     */
    @DeleteMapping("/items/{skuId}")
    public ApiResponse<ArrayList<CartItem>> delete(HttpServletRequest request, @PathVariable Long skuId) {
        Long userId = UserContext.from(request).getUserId();
        Map<Long, CartItem> userCart = carts.get(userId);
        if (userCart != null) {
            userCart.remove(skuId);
        }
        return get(request);
    }
}
