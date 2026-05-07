package com.paymentsystem.message;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * 消息接口控制器，提供当前用户站内消息查询能力。
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {
    /**
     * 查询演示站内消息列表。
     *
     * @return 消息列表
     */
    @GetMapping
    public ApiResponse<List<MessageView>> list() {
        return ApiResponse.ok(Arrays.asList(new MessageView(1L, "订单支付成功", "你的订单已支付，商户将尽快处理。", Instant.now())));
    }
}
