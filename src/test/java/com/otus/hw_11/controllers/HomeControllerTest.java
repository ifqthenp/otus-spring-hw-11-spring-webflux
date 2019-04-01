package com.otus.hw_11.controllers;

import com.otus.hw_11.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(HomeController.class)
@Import(ThymeleafAutoConfiguration.class)
class HomeControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private BookService bookService;

    @Test
    void shouldDisplayCorrectLibraryTotals() {
        when(bookService.getBooksCount()).thenReturn(Mono.just(5L));
        client.get()
            .uri("/home")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(containsString("Total books in the library: <span>5</span>"));
    }

    @Test
    void shouldDisplayCorrectAuthorTotals() {
        when(bookService.getAuthorsCount()).thenReturn(Mono.just(10L));
        client.get()
            .uri("/home")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(containsString("Total authors in the library: <span>10</span>"));
    }

    @Test
    void shouldDisplayCorrectGenreTotals() {
        when(bookService.getGenresCount()).thenReturn(Mono.just(20L));
        client.get()
            .uri("/home")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(containsString("Total genres in the library: <span>20</span>"));
    }

}
