package com.otus.hw_11.controllers;

import com.otus.hw_11.domain.Book;
import com.otus.hw_11.dto.BookAddFormDto;
import com.otus.hw_11.services.BookService;
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
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BookAddFormController.class)
@Import(ThymeleafAutoConfiguration.class)
class BookAddFormControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private BookService service;

    private BookAddFormDto addFormDto;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("controller can return a view with form")
    void getForm() {
        client.get()
            .uri("/library/books/add")
            .accept(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(String.class)
            .value(containsString("<h3>Please use this form to add a book to the library</h3>"));
    }

    @Test
    @DisplayName("users can save a book via form")
    void bookAddForm() {
        final BookAddFormDto formDto = new BookAddFormDto();
        formDto.setTitle("Time Machine");
        formDto.setGenre("Novel");
        formDto.setFirstName("Herbert");
        formDto.setLastName("Wells");
        formDto.setYear("1999");
        final Book book = formDto.toEntity(formDto);
        when(service.saveBook(book)).thenReturn(Mono.just(book));

        final EntityExchangeResult<String> returnResult =
            client.post()
            .uri(builder -> builder
                .path("/library/books/add")
                .queryParam("title", formDto.getTitle())
                .queryParam("firstName", formDto.getFirstName())
                .queryParam("lastName", formDto.getLastName())
                .queryParam("genre", formDto.getGenre())
                .queryParam("year", formDto.getYear())
                .build())
            .contentType(MediaType.TEXT_HTML)
            .exchange()
            .expectStatus()
            .isSeeOther()
            .expectBody(String.class)
            .returnResult();

        verify(service).saveBook(book);
        verifyNoMoreInteractions(service);
        assertThat(returnResult.getResponseHeaders().toString())
            .contains("[Location:\"/home\"]");
    }

}
