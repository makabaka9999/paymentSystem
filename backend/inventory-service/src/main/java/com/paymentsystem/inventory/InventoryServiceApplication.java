package com.paymentsystem.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 库存服务启动类。
 */
@SpringBootApplication
public class InventoryServiceApplication {
    /**
     * 启动库存服务。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
