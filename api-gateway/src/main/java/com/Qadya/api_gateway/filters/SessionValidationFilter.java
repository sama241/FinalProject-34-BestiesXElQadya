package com.Qadya.api_gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class GlobalAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // Allow public endpoints without auth
        if (isPublicEndpoint(path)) {
            return chain.filter(exchange);
        }

        // Extract SESSION cookie
        List<String> cookies = exchange.getRequest().getCookies().getFirst("SESSION") != null ?
                List.of(exchange.getRequest().getCookies().getFirst("SESSION").getValue()) : null;

        if (cookies == null || cookies.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String sessionId = cookies.get(0);

        // Decide whether it's user or worker path
        boolean isWorkerPath = path.startsWith("/workers") || path.startsWith("/api/worker/");
        String validationUri = isWorkerPath
                ? "http://worker-service:8082/api/worker/auth/me"
                : "http://userservice-app-1:8081/api/user/auth/me";

        // Call the /me endpoint to validate session
        return webClientBuilder.build()
                .get()
                .uri(validationUri)
                .cookie("SESSION", sessionId)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> Mono.error(new RuntimeException("Session invalid or expired"))
                )
                .toBodilessEntity()
                .flatMap(resp -> chain.filter(exchange))
                .onErrorResume(ex -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/user/auth/login") ||
                path.equals("/api/worker/auth/login") ||
                path.startsWith("/search") ||
                path.equals("/users") ||
                path.equals("/workers");
    }

    @Override
    public int getOrder() {
        return -1; // High precedence
    }
}