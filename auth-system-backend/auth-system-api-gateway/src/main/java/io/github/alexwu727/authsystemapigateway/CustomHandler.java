package io.github.alexwu727.authsystemapigateway;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CustomHandler {
    public Mono<ServerResponse> getHello(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("Hello, Reactive Gateway!"), String.class);
    }

    // post
    public Mono<ServerResponse> postHello(ServerRequest request) {
        return ServerResponse.ok().body(Mono.just("Hello, Reactive Gateway!"), String.class);
    }
}
