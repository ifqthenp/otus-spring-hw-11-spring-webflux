package com.otus.hw_11.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    private static final String REST_LIBRARY_BOOKS = "/rest/library/books";
    private static final String REST_LIBRARY_BOOKS_ID = "/rest/library/books/{id}";

    @Bean
    RouterFunction<ServerResponse> genreRoutes(final BookHandler handler) {
        //        return route(GET(REST_LIBRARY_BOOKS).and(accept(APPLICATION_JSON)), handler::getAllBooks)
        //            .andRoute(GET(REST_LIBRARY_BOOKS_ID).and(accept(APPLICATION_JSON)), handler::getBook)
        //            .andRoute(POST(REST_LIBRARY_BOOKS).and(accept(APPLICATION_JSON)), handler::saveBook)
        //            .andRoute(PUT(REST_LIBRARY_BOOKS_ID).and(accept(APPLICATION_JSON)), handler::updateBook)
        //            .andRoute(DELETE(REST_LIBRARY_BOOKS_ID).and(accept(APPLICATION_JSON)), handler::deleteBook)
        //            .andRoute(DELETE(REST_LIBRARY_BOOKS).and(accept(APPLICATION_JSON)), handler::deleteAllBooks);

        return nest(path(REST_LIBRARY_BOOKS),
            nest(accept(APPLICATION_JSON).or(contentType(APPLICATION_JSON)),
                route(GET("/"), handler::getAllBooks)
                    .andRoute(method(HttpMethod.POST), handler::saveBook)
                    .andRoute(DELETE("/"), handler::deleteAllBooks)
                    .andNest(path("/{id}"),
                        route(method(HttpMethod.GET), handler::getBook)
                            .andRoute(method(HttpMethod.PUT), handler::updateBook)
                            .andRoute(method(HttpMethod.DELETE), handler::deleteBook))));
    }

}
