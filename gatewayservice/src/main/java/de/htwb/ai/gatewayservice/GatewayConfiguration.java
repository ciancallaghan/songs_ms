package de.htwb.ai.gatewayservice;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("songservice", r -> r.path("/song/**").uri("lb://SONGSERVICE"))
                .route("songservice", r -> r.path("/playlist/**").uri("lb://SONGSERVICE"))
                .route("authservice", r -> r.path("/auth/**").uri("lb://AUTHSERVICE"))
                .route("lyricservice", r -> r.path("/lyric/**").uri("lb://LYRICSERVICE"))
                .build();
    }
}
