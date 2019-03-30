package com.otus.hw_11.rest;

import com.otus.hw_11.domain.Book;
import com.otus.hw_11.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BookHandler {

    private final BookService service;

    public Mono<ServerResponse> getAllBooks(ServerRequest request) {
        Flux<Book> genreFlux = service.findAll();
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(genreFlux, Book.class);
    }

    public Mono<ServerResponse> getBook(ServerRequest request) {
        final String id = request.pathVariable("id");
        final Mono<Book> bookMono = service.findById(id);
        final Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return bookMono.flatMap(book ->
            ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(book)))
            .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> saveBook(ServerRequest request) {
        final Mono<Book> bookMono = request.bodyToMono(Book.class);
        return bookMono.flatMap(book ->
            ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.saveBook(book), Book.class));
    }

}
