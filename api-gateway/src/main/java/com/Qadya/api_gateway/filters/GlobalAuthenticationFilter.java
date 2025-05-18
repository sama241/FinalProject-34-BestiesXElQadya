//package com.Qadya.api_gateway.filters;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class GlobalAuthenticationFilter implements GlobalFilter, Ordered {
//
//    @Autowired
//    private WebClient.Builder webClientBuilder;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getPath().value();
//
//        if (isPublicEndpoint(path)) {
//            return chain.filter(exchange);
//        }
//
//        // Extract SESSION cookie
//        String sessionId = exchange.getRequest().getCookies()
//                .getFirst("SESSION") != null
//                ? exchange.getRequest().getCookies().getFirst("SESSION").getValue()
//                : null;
//
//        if (sessionId == null) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        // Determine the correct service to validate the session
//        String validationUri = path.startsWith("/workers")
//                ? "http://workerservice:8082/api/worker/auth/me"
//                : "http://userservice-app-1:8081/api/user/auth/me";
//
//        // Validate session and set ID header
//        return webClientBuilder.build()
//                .get()
//                .uri(validationUri)
//                .cookie("SESSION", sessionId)
//                .retrieve()
//                .bodyToMono(String.class)
//
//                .flatMap(responseBody -> {
//                    System.out.println("Setting X-User-Id header: " + responseBody.trim());
//
//                    ServerHttpRequest newRequest = exchange.getRequest()
//                            .mutate()
//                            .headers(headers -> headers.set("X-User-Id", responseBody.trim()))
//                            .build();
//
//                    ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
//                    return chain.filter(newExchange);
//                })
//                .onErrorResume(error -> {
//                    String errorMessage = "Unauthorized: Invalid or expired session. Please log in again.";
//                    System.out.println(errorMessage);
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    byte[] responseMessage = errorMessage.getBytes();
//                    return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
//                            .bufferFactory().wrap(responseMessage)));
//                });
//    }
//
//    private boolean isPublicEndpoint(String path) {
//        return path.equals("/api/user/auth/login")
//                || path.equals("/api/worker/auth/login")
//                || path.startsWith("/search")
//                || path.equals("/users")
//                || path.equals("/workers/create");
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}


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

        // Determine the correct service to validate the session
        String validationUri = path.startsWith("/workers")
                ? "http://workerservice:8082/api/worker/auth/me"
                : "http://userservice-app-1:8081/api/user/auth/me";

        // Make the validation call to the backend /me endpoint
        return webClientBuilder.build()
                .get()
                .uri(validationUri)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(responseBody -> {
                    // Set the ID as a header for downstream services
                    String userId = responseBody.trim();
                    ServerHttpRequest newRequest = exchange.getRequest()
                            .mutate()
                            .headers(headers -> headers.set("X-User-Id", userId))
                            .build();

                    ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
                    return chain.filter(newExchange);
                })
                .onErrorResume(error -> {
                    // Handle authentication failure
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    private boolean isPublicEndpoint(String path) {
        return path.equals("/api/user/auth/login")
                || path.equals("/api/worker/auth/login")
                || path.startsWith("/search")
                || path.equals("/users")
                || path.equals("/workers/create");
    }

    @Override
    public int getOrder() {
        return -1;
    }
}

