package io.github.alexwu727.authsystemapigateway.config;

import io.github.alexwu727.authsystemapigateway.util.JwtUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

// The @Component annotation is a class-level annotation which tells Spring to automatically detect and manage these classes. It's typically used when you write your own class and want Spring to create and manage the instance of that class.
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements WebFilter {
    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        final String jwt;
        final String userId;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }
        jwt = authHeader.substring(7);
        try {
            userId = jwtUtil.extractUserId(jwt);
        } catch (Exception e) {
            return chain.filter(exchange);
        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, null);
                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate().header("User-Id", userId).build();
                return chain.filter(exchange.mutate().request(mutatedRequest).build())
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        }
        return chain.filter(exchange);
    }
}

