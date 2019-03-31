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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

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

    @Test
    @DisplayName("returns 404 if cannot find book by ID")
    void testIfBookIdNotFound() {
        final String fakeId = "test";
        when(service.findById(fakeId)).thenReturn(Mono.empty());
        client.get()
            .uri("/{id}", fakeId)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    @DisplayName("can save a book")
    void testSaveBook() {
        final Book aBook = getBook();
        final Mono<Book> bookMono = Mono.just(aBook);
        when(service.saveBook(aBook)).thenReturn(bookMono);

        client.post()
            .uri("/")
            .contentType(APPLICATION_JSON)
            .body(bookMono, Book.class)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Book.class)
            .isEqualTo(aBook);

        verify(service).saveBook(aBook);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("can update a book")
    void testUpdateBook() {
        final Book existingBook = getBook();
        when(service.findById(anyString())).thenReturn(Mono.just(existingBook));

        final Book updatedBook = getBook();
        updatedBook.setTitle("Updated Title");
        final Mono<Book> updatedBookMono = Mono.just(updatedBook);
        when(service.saveBook(any())).thenReturn(updatedBookMono);

        client.put()
            .uri("/{id}", existingBook.getId())
            .accept(APPLICATION_JSON)
            .body(updatedBookMono, Book.class)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(Book.class)
            .isEqualTo(updatedBook);

        verify(service).findById(anyString());
        verify(service).saveBook(any());
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("cannot update book if its ID does not exist")
    void testUpdateBookNonExistId() {
        final String fakeId = "fakeId";
        when(service.findById(fakeId)).thenReturn(Mono.empty());

        final Book updatedBook = getBook();
        updatedBook.setTitle("Updated Title");
        final Mono<Book> updatedBookMono = Mono.just(updatedBook);
        when(service.saveBook(any())).thenReturn(updatedBookMono);

        client.put()
            .uri("/{id}", fakeId)
            .accept(APPLICATION_JSON)
            .body(updatedBookMono, Book.class)
            .exchange()
            .expectStatus()
            .isNotFound();

        verify(service).findById(fakeId);
        verify(service, times(0)).saveBook(updatedBook);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("can delete a book")
    void testDeleteBook() {
        final Book aBook = getBook();
        final String bookId = aBook.getId().toHexString();
        when(service.findById(bookId)).thenReturn(Mono.just(aBook));
        when(service.delete(aBook)).thenReturn(Mono.empty());

        client.delete()
            .uri("/{id}", bookId)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk();

        verify(service).findById(anyString());
        verify(service).delete(aBook);
        verifyZeroInteractions(service);
    }

    @Test
    @DisplayName("cannot delete a book if its ID does not exist")
    void testDeleteBookIdNotFound() {
        final Book aBook = getBook();
        final String bookId = "non-existing-id";
        when(service.findById(bookId)).thenReturn(Mono.empty());
        when(service.delete(aBook)).thenReturn(Mono.empty());

        client.delete()
            .uri("/{id}", bookId)
            .accept(APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();

        verify(service).findById(anyString());
        verify(service, times(0)).delete(aBook);
        verifyZeroInteractions(service);
    }


    private Book getBook() {
        // @formatter:off
        return new Book(new ObjectId("5c857854402f511692419328"),
            "The Book of Calculation", "1202",
            new ArrayList<>() {{ add(new Author("Leonardo", "Fibonacci")); }},
            new HashSet<>() {{ add(new Genre("Mathematics")); }}, new ArrayList<>());
        // @formatter:on
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
