package com.paymentsystem.message;

import com.paymentsystem.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @GetMapping
    public ApiResponse<List<MessageView>> list() {
        return ApiResponse.ok(Arrays.asList(new MessageView(1L, "订单支付成功", "你的订单已支付，商户将尽快处理。", Instant.now())));
    }

    public static class MessageView {
        private Long id;
        private String title;
        private String content;
        private Instant createdAt;
        public MessageView(Long id, String title, String content, Instant createdAt) {
            this.id = id; this.title = title; this.content = content; this.createdAt = createdAt;
        }
        public Long getId() { return id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public Instant getCreatedAt() { return createdAt; }
    }
}
