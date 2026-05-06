package com.paymentsystem.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class DemoJwtHeaderFilter implements GlobalFilter, Ordered {
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

    @Override
    public int getOrder() {
        return -100;
    }
}
