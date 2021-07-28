package com.example.spring.boot.reactor.handler;

import com.example.spring.boot.reactor.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
@AllArgsConstructor
public class UserHandler {

    private final UserRepository userRepository;
    private final Scheduler jdbcScheduler;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return Flux.defer(() -> Flux.fromIterable(userRepository.findAll()))
            .subscribeOn(jdbcScheduler)
            .collectList()
            .flatMap(users -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(users))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findByName(ServerRequest request) {
        return request.queryParam("name").map(name ->
            Mono.fromCallable(() -> userRepository.findByName(name))
                .subscribeOn(jdbcScheduler)
                .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
                .switchIfEmpty(ServerResponse.notFound().build())
        ).orElse(ServerResponse.notFound().build());
    }
}
