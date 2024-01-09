package dev.manyroads.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/*
Class for testing the application
 */
@RestController
public class TestController {

    Logger logger = LogManager.getLogger(TestController.class);

    @GetMapping("/test")
    public Mono<String> testing(Mono<Authentication> auth) {

        logger.info("/test");

        return auth.map(a -> "Holita " + a.getName());
    }

    @GetMapping("/test2")
    public Mono<String> testing() {

        logger.info("/test2");

        return Mono.just("Holita2");
    }
}
