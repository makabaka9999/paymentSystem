package com.paymentsystem.cart.controller;

import com.paymentsystem.cart.dto.CartCommand;
import com.paymentsystem.cart.dto.CartItem;
import com.paymentsystem.cart.repository.CartRepository;
import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.common.web.UserContext;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartRepository cartRepository;

    /**
     * 创建购物车控制器并注入购物车仓储。
     *
     * @param cartRepository 购物车数据访问对象
     */
    public CartController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    /**
     * 查询当前用户的购物车明细。
     *
     * @param request HTTP 请求对象
     * @return 当前用户购物车
     */
    @GetMapping
    public ApiResponse<List<CartItem>> get(HttpServletRequest request) {
        Long userId = UserContext.from(request).getUserId();
        return ApiResponse.ok(cartRepository.findByUserId(userId));
    }

    /**
     * 添加商品到当前用户购物车。
     *
     * @param request HTTP 请求对象
     * @param command 添加购物车参数
     * @return 更新后的购物车
     */
    @PostMapping("/items")
    public ApiResponse<List<CartItem>> add(HttpServletRequest request, @RequestBody CartCommand command) {
        Long userId = UserContext.from(request).getUserId();
        cartRepository.add(userId, command.toItem());
        return get(request);
    }

    /**
     * 修改当前用户购物车中指定 SKU 的数量。
     *
     * @param request HTTP 请求对象
     * @param skuId SKU ID
     * @param command 数量更新参数
     * @return 更新后的购物车
     */
    @PutMapping("/items/{skuId}")
    public ApiResponse<List<CartItem>> update(HttpServletRequest request, @PathVariable Long skuId, @RequestBody CartCommand command) {
        Long userId = UserContext.from(request).getUserId();
        cartRepository.updateQuantity(userId, skuId, command.getQuantity());
        return get(request);
    }

    /**
     * 删除当前用户购物车中指定 SKU。
     *
     * @param request HTTP 请求对象
     * @param skuId SKU ID
     * @return 更新后的购物车
     */
    @DeleteMapping("/items/{skuId}")
    public ApiResponse<List<CartItem>> delete(HttpServletRequest request, @PathVariable Long skuId) {
        Long userId = UserContext.from(request).getUserId();
        cartRepository.delete(userId, skuId);
        return get(request);
    }
}
