package com.example.spring.boot.reactor.router;

import java.util.Objects;

import com.example.spring.boot.reactor.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class UserRouter {

    private static final String BASE_URL = "/user";

    @Bean
    RouterFunction<ServerResponse> userRoute(UserHandler handler) {
        return RouterFunctions.route(
            RequestPredicates.GET(BASE_URL + "/find")
                .and(RequestPredicates.queryParam("name", Objects::nonNull)),
            handler::findByName
        ).andRoute(
            RequestPredicates.GET(BASE_URL + "/findAll"),
            handler::findAll
        ).andRoute(
            RequestPredicates.POST(BASE_URL + "/create"),
            handler::create
        );
    }
}
