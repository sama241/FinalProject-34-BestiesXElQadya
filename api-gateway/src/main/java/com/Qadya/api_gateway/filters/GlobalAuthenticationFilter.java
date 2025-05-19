package com.Qadya.api_gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class GlobalAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Allow public endpoints without authentication
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Determine the correct authentication endpoint and header based on the path prefix
        String authServiceUri;
        String headerName;

        if (path.startsWith("/workers") || path.startsWith("/reviews/worker") || path.startsWith("/bookings/worker")) {
            authServiceUri = "http://worker-service:8082/api/worker/auth/me";
            headerName = "X-Worker-Id";
        } else if (path.startsWith("/users") || path.startsWith("/reviews/user") || path.startsWith("/bookings/user")) {
            authServiceUri = "http://user-service:8081/api/user/auth/me";
            headerName = "X-User-Id";
        }else {
            return unauthorized(exchange);
        }

        // Extract and validate the session ID
        String sessionId = exchange.getRequest().getHeaders().getFirst("Session-Id");
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return unauthorized(exchange);
        }

        // Validate the session
        return webClientBuilder.build()
                .get()
                .uri(authServiceUri + "?sessionId=" + sessionId)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(userId -> {
                    if (userId == null || userId.trim().isEmpty()) {
                        return unauthorized(exchange);
                    }

                    // Add the correct user or worker ID to the headers
                    ServerHttpRequest updatedRequest = exchange.getRequest()
                            .mutate()
                            .header(headerName, userId.trim())
                            .build();

                    return chain.filter(exchange.mutate().request(updatedRequest).build());
                })
                .onErrorResume(error -> unauthorized(exchange));
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/user/auth/login")
                || path.equals("/api/worker/auth/login")
                || path.startsWith("/search")
                || path.equals("/users/create")
                || path.startsWith("/users/get")
                || path.equals("/workers/create")
                || path.equals("/bookings/get")
                || path.startsWith("/workers/get")
                || path.startsWith("/reviews/get")
                || path.equals("/users");

    }

    @Override
    public int getOrder() {
        return -1;
    }
}