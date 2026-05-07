package com.paymentsystem.product.controller;

import com.paymentsystem.common.ApiResponse;
import com.paymentsystem.product.dto.ProductView;
import com.paymentsystem.product.repository.ProductRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductRepository productRepository;

    /**
     * 创建商品控制器并注入商品仓储。
     *
     * @param productRepository 商品数据访问对象
     */
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 查询上架商品列表。
     *
     * @param keyword 搜索关键字
     * @return 商品列表
     */
    @GetMapping
    public ApiResponse<List<ProductView>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.ok(productRepository.findOnSale(keyword));
    }

    /**
     * 查询指定商品详情。
     *
     * @param id 商品 ID
     * @return 商品详情或未找到错误
     */
    @GetMapping("/{id}")
    public ApiResponse<ProductView> detail(@PathVariable Long id) {
        ProductView product = productRepository.findOnSaleById(id);
        if (product == null) {
            return ApiResponse.fail("PRODUCT_NOT_FOUND", "product is not available");
        }
        return ApiResponse.ok(product);
    }
}
