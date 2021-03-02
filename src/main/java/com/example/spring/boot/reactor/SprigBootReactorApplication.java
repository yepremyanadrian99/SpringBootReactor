package com.example.spring.boot.reactor;

import com.example.spring.boot.reactor.client.GreetingWebClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SprigBootReactorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SprigBootReactorApplication.class, args);

        GreetingWebClient greetingWebClient = new GreetingWebClient();
        System.out.println(greetingWebClient.getResult());
    }

}
