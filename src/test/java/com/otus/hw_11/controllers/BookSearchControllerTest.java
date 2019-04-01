package com.otus.hw_11.controllers;

import com.otus.hw_11.domain.Author;
import com.otus.hw_11.domain.Genre;
import com.otus.hw_11.dto.BookSearchResultDto;
import com.otus.hw_11.services.BookService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import reactor.core.publisher.Flux;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BookSearchController.class)
@Import(ThymeleafAutoConfiguration.class)
class BookSearchControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private BookService service;

    private Flux<BookSearchResultDto> books;

    @BeforeEach
    void setUp() {
        books = Flux.fromIterable(
            new ArrayList<>() {{
                add(new BookSearchResultDto("Time Machine", new ArrayList<>() {{
                    add(new Author("Herbert", "Wells"));
                }}, new ArrayList<>() {{
                    add(new Genre("Novel"));
                    add(new Genre("Science-Fiction"));
                }}));
                add(new BookSearchResultDto("Love in the Time of Cholera", new ArrayList<>() {{
                    add(new Author("Gabriel", "Garcia Marquez"));
                }}, new ArrayList<>() {{
                    add(new Genre("Novel"));
                }}));
            }}
        );
    }

    @Test
    @DisplayName("controller can return search form view")
    void fullSearchForm() {
        client.get()
            .uri("/library/books/search")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(Matchers.containsString("Please use this form to start searching"));
    }

    @Test
    @DisplayName("users can find a book by title")
    void searchByTitle() {
        final String requestParam = "time";
        when(service.findBooksByTitleRequestParam(requestParam)).thenReturn(books);
        client.get()
            .uri(builder -> builder.path("/library/books/search/title")
                .queryParam("title", requestParam)
                .build())
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(Matchers.containsString("Time Machine"));
    }

    @Test
    @DisplayName("users can find a book by author's last name")
    void searchByAuthor() {
        final String requestParam = "Wells";
        when(service.findBooksByAuthorRequestParam(requestParam)).thenReturn(books);
        client.get()
            .uri(builder -> builder.path("/library/books/search/author")
                .queryParam("lastName", requestParam)
                .build())
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(Matchers.containsString("Herbert Wells"));
    }

    @Test
    @DisplayName("users can find a book by genre name")
    void searchByGenre() {
        final String requestParam = "Novel";
        when(service.findBooksByGenreName(requestParam)).thenReturn(books);
        client.get()
            .uri(builder -> builder.path("/library/books/search/genre")
                .queryParam("genre", requestParam)
                .build())
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(Matchers.containsString("<td>Novel</td>"));
    }

}
