package com.paymentsystem.merchant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 商户服务启动类。
 */
@SpringBootApplication
public class MerchantServiceApplication {
    /**
     * 启动商户服务。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MerchantServiceApplication.class, args);
    }
}
