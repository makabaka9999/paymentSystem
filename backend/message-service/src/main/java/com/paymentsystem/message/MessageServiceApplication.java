package com.paymentsystem.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 消息服务启动类。
 */
@SpringBootApplication
public class MessageServiceApplication {
    /**
     * 启动消息服务。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
    }
}
