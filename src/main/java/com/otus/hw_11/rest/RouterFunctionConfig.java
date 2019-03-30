package com.otus.hw_11.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    @Bean
    RouterFunction<ServerResponse> genreRoutes(final BookHandler handler) {
        return route(GET("/rest/library/books").and(accept(APPLICATION_JSON)), handler::getAllBooks)
            .andRoute(GET("/rest/library/books/{id}").and(accept(APPLICATION_JSON)), handler::getBook);
    }

}
