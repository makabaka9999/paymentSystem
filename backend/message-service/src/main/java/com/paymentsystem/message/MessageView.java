package com.paymentsystem.message;

import java.time.Instant;

/**
 * 站内消息响应视图。
 */
public class MessageView {
    /** 消息 ID。 */
    private Long id;
    /** 消息标题。 */
    private String title;
    /** 消息内容。 */
    private String content;
    /** 消息创建时间。 */
    private Instant createdAt;

    /**
     * 创建站内消息视图。
     */
    public MessageView(Long id, String title, String content, Instant createdAt) {
        this.id = id; this.title = title; this.content = content; this.createdAt = createdAt;
    }

    /** @return 消息 ID */
    public Long getId() { return id; }
    /** @return 消息标题 */
    public String getTitle() { return title; }
    /** @return 消息内容 */
    public String getContent() { return content; }
    /** @return 消息创建时间 */
    public Instant getCreatedAt() { return createdAt; }
}
