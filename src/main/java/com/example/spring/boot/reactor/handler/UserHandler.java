package com.example.spring.boot.reactor.handler;

import com.example.spring.boot.reactor.dto.UserDTO;
import com.example.spring.boot.reactor.exception.InvalidParameterException;
import com.example.spring.boot.reactor.mapper.UserMapper;
import com.example.spring.boot.reactor.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return Flux.defer(() -> Flux.fromIterable(userRepository.findAll()))
            .subscribeOn(jdbcScheduler)
            .collectList()
            .flatMap(users -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(users))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Transactional(readOnly = true)
    public Mono<ServerResponse> findByName(ServerRequest request) {
        String name = request.queryParam("name").orElseThrow(InvalidParameterException::new);
        return Mono.fromCallable(() -> userRepository.findByName(name))
            .subscribeOn(jdbcScheduler)
            .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    @Transactional
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(UserDTO.class)
            .map(userMapper::mapToUser)
            .doOnNext(userRepository::save)
            .subscribeOn(jdbcScheduler)
            .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
            .switchIfEmpty(ServerResponse.notFound().build());
    }
}
