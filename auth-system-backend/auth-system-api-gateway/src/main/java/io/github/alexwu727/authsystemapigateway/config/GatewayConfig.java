package io.github.alexwu727.authsystemapigateway.config;

import io.github.alexwu727.authsystemapigateway.CustomHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GatewayConfig {
    @Bean
    public RouterFunction<ServerResponse> route(CustomHandler customHandler) {
        return RouterFunctions.route()
                .GET("/helloReactiveGateway", customHandler::getHello)
                .POST("/helloReactiveGateway", customHandler::postHello)
                .build();
    }
}
