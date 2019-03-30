package com.otus.hw_11.rest;

import com.otus.hw_11.domain.Author;
import com.otus.hw_11.domain.Book;
import com.otus.hw_11.domain.Comment;
import com.otus.hw_11.domain.Genre;
import com.otus.hw_11.services.BookService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {
    BookHandler.class,
    RouterFunctionConfig.class
})
class RouterFunctionConfigTest {

    private WebTestClient client;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private BookService service;

    private List<Book> expectedBooks;

    @BeforeEach
    void setUp() {
        client = WebTestClient
            .bindToApplicationContext(context)
            .configureClient()
            .baseUrl("/rest/library/books")
            .build();
        expectedBooks = getBooksFlux().collectList().block();
    }

    @Test
    @DisplayName("can get all books from repository")
    void testGetAllBooks() {
        when(service.findAll()).thenReturn(getBooksFlux());
        client.get().uri("/")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(Book.class)
            .hasSize(3)
            .isEqualTo(expectedBooks);
    }

    @Test
    @DisplayName("can get a book by id")
    void testIfProductIdFound() {
        final int aliceIdx = 0;
        final String aliceId = "5c857854402f51169241931f";
        when(service.findById(aliceId)).thenReturn(getBooksFlux().elementAt(aliceIdx));

        client.get().uri("/{id}", aliceId)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Book.class)
            .isEqualTo(expectedBooks.get(aliceIdx));
    }

    private Flux<Book> getBooksFlux() {
        // @formatter:off
        return Flux.just(
            new Book(new ObjectId("5c857854402f51169241931f"), "Alice in Wonderland", "1865",
                new ArrayList<>() {{ add(new Author("Lewis", "Carrol")); }},
                new HashSet<>() {{ add(new Genre("Children's Literature")); add(new Genre("Fantasy")); }},
                new ArrayList<>() {{ add(new Comment(1, "excellent")); add(new Comment(2, "awesome book")); }}),
            new Book(new ObjectId("5c857854402f511692419322"), "The Time Machine", "1895",
                new ArrayList<>() {{ add(new Author("Herbert", "Wells")); }},
                new HashSet<>() {{ add(new Genre("Science-Fiction")); }},
                new ArrayList<>() {{ add(new Comment(1, "great")); }}),
            new Book(new ObjectId("5c857854402f511692419325"), "Childhood", "1852",
                new ArrayList<>() {{ add(new Author("Leo", "Tolstoy")); }},
                new HashSet<>() {{ add(new Genre("Autobiography")); add(new Genre("Literary Realism")); add(new Genre("Novel")); }},
                new ArrayList<>())
        );
        // @formatter:on
    }

}