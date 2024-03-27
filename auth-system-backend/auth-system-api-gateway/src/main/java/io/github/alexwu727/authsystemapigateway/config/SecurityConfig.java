package io.github.alexwu727.authsystemapigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(
                        requests ->
                                requests
//                                        .pathMatchers("/api/v1/auth/**").permitAll()
                                        .pathMatchers("/api/v1/auth/register").permitAll()
                                        .pathMatchers("/api/v1/auth/login").permitAll()
                                        .pathMatchers("/api/v1/auth/verify").permitAll()
                                        .pathMatchers("/api/v1/auth/resend-verification-code").permitAll()
                                        .pathMatchers("/api/v1/auth/forgot-password").permitAll()
                                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        .pathMatchers("/helloReactiveGateway/**").permitAll()
                                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }
}
