package com.ranim.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    /*
     * PDF 04: routes configured with RouteLocator (alternative to the routes of application.yml).
     * To use it: remove the "routes" block of application.yml, add the imports
     * (org.springframework.cloud.gateway.route.RouteLocator, org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder,
     * org.springframework.context.annotation.Bean) and uncomment:
     *
     * @Bean
     * public RouteLocator MyRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
     *     return routeLocatorBuilder.routes()
     *             .route(p -> p
     *                     .path("/api/brands/**")
     *                     .uri("lb://BRAND"))
     *             .route(p -> p
     *                     .path("/api/products/**")
     *                     .uri("lb://PRODUCT"))
     *             .build();
     * }
     */
}
