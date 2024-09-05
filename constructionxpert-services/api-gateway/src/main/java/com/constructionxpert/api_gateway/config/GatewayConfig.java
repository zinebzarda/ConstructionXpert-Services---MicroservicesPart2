package com.constructionxpert.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/users/**")
                        .uri("lb://constructionxpert-user-management-service"))
                .route("project-service", r -> r.path("/projects/**")
                        .uri("lb://constructionxpert-project-service"))
                .route("task-service", r -> r.path("/tasks/**")
                        .uri("lb://constructionxpert-task-service"))
                .route("resource-service", r -> r.path("/resources/**")
                        .uri("lb://constructionxpert-resource-service"))
                .build();
    }
}