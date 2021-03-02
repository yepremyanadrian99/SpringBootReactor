package com.example.spring.boot.reactor.router;

import com.example.spring.boot.reactor.handler.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(GreetingHandler handler) {
        return RouterFunctions.route(
            RequestPredicates.GET("/helloWorld")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
            handler::greeting
        );
    }

}
