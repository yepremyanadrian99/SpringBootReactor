package com.example.spring.boot.reactor.client;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class GreetingWebClient {

    private final WebClient client = WebClient.create("http://localhost:9010");

    private final Mono<String> result = client
        .get().uri("/helloWorld")
        .accept(MediaType.TEXT_PLAIN)
        .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class));

    public String getResult() {
        String message = result.block();
        return String.format(">> result = %s <<", message);
    }

}
