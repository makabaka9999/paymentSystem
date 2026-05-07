package com.paymentsystem.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 演示 JWT 请求头过滤器，将 demo token 中的用户信息转成内部请求头。
 */
@Component
public class DemoJwtHeaderFilter implements GlobalFilter, Ordered {
    /**
     * 解析 Authorization 请求头并追加用户上下文请求头。
     *
     * @param exchange 网关交换上下文
     * @param chain 过滤器链
     * @return 异步处理结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        ServerHttpRequest.Builder request = exchange.getRequest().mutate();
        if (token != null && token.startsWith("Bearer demo.")) {
            String[] parts = token.substring("Bearer demo.".length()).split("\\.");
            if (parts.length >= 2) {
                request.header("X-User-Id", parts[0]);
                request.header("X-User-Role", parts[1]);
                request.header("X-User-Name", "demo-user");
            }
        }
        return chain.filter(exchange.mutate().request(request.build()).build());
    }

    /**
     * 设置过滤器执行顺序，确保较早注入用户请求头。
     *
     * @return 过滤器顺序
     */
    @Override
    public int getOrder() {
        return -100;
    }
}
