package com.Qadya.api_gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class SessionValidationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        // Skip validation for public endpoints
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        return exchange.getSession()
                .flatMap(session -> {
                    if (session.isExpired()) {
                        return unauthorized(exchange, "Session expired or invalid");
                    }

                    // Add session info to downstream requests
                    ServerHttpRequest request = exchange.getRequest().mutate()
                            .header("X-Session-User", session.getId())
                            .build();

                    return chain.filter(exchange.mutate().request(request).build());
                });
    }

    private boolean isPublicEndpoint(String path) {
        return path.contains("/users/login") ||
                path.contains("/users/register") ||
                path.contains("/workers/login") ||
                path.contains("/workers/register");
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}