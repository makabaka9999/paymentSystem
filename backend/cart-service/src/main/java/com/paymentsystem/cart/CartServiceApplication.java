package com.paymentsystem.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 购物车服务启动类。
 */
@SpringBootApplication
public class CartServiceApplication {
    /**
     * 启动购物车服务。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CartServiceApplication.class, args);
    }
}
