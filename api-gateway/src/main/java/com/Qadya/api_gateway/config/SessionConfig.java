//package com.Qadya.api_gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebSession;
//import org.springframework.web.server.session.WebSessionManager;
//import reactor.core.publisher.Mono;
//
//@Configuration
//public class SessionConfig {
//
//    @Bean
//    public WebSessionManager webSessionManager() {
//        return new WebSessionManager() {
//            @Override
//            public Mono<WebSession> getSession(ServerWebExchange exchange) {
//                return exchange.getSession();
//            }
//        };
//    }
//}