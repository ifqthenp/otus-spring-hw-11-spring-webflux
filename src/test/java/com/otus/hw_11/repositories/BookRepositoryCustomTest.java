package com.otus.hw_11.repositories;

import com.otus.hw_11.domain.Author;
import com.otus.hw_11.domain.Book;
import com.otus.hw_11.domain.Genre;
import org.bson.types.ObjectId;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.HashSet;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class BookRepositoryCustomTest {

    @Autowired
    private BookRepositoryCustom bookRepository;

    @Autowired
    private MongoTemplate template;

    @BeforeEach
    void setUp() {
        final ArrayList<Book> testBooks = getBooks();
        this.template.insertAll(testBooks);
    }

    @AfterEach
    void tearDown() {
        this.template.dropCollection(Book.class);
    }

    @Test
    @DisplayName("should return flux of distinct authors")
    void testReturnFluxDistinctAuthors() {
        final Flux<String> authors = bookRepository.findDistinctAuthorLastName();
        MatcherAssert.assertThat(authors.count().block(), Matchers.is(4L));
    }

    @Test
    @DisplayName("should return flux of distinct genres")
    void testReturnFluxDistinctGenres() {
        final Flux<String> genres = bookRepository.findDistinctGenres();
        MatcherAssert.assertThat(genres.count().block(), Matchers.is(3L));
    }

    private ArrayList<Book> getBooks() {
        // @formatter:off
        return new ArrayList<>() {{
            add(new Book(new ObjectId("5c857854402f511692419327"),
                "Love in the Time of Cholera", "1985",
                new ArrayList<>() {{ add(new Author("Gabriel", "García Márquez")); }},
                new HashSet<>() {{ add(new Genre("Novel")); }},
                new ArrayList<>()));
            add(new Book(new ObjectId("5c857854402f511692419328"),
                "The Book of Calculation", "1202",
                new ArrayList<>() {{ add(new Author("Leonardo", "Fibonacci")); }},
                new HashSet<>() {{ add(new Genre("Mathematics")); }},
                new ArrayList<>()));
            add(new Book(new ObjectId("5c857854402f511692419329"),
                "The Twelve Chairs", "1928",
                new ArrayList<>() {{ add(new Author("Ilya", "Ilf")); add(new Author("Yevgeni", "Petrov")); }},
                new HashSet<>() {{ add(new Genre("Satire")); add(new Genre("Novel")); }},
                new ArrayList<>()));
            add(new Book(new ObjectId("5c857854402f51169241932a"),
                "The Little Golden Calf", "1931",
                new ArrayList<>() {{ add(new Author("Ilya", "Ilf")); add(new Author("Yevgeni", "Petrov")); }},
                new HashSet<>() {{ add(new Genre("Satire")); add(new Genre("Novel")); }},
                new ArrayList<>()));
        }};
        // @formatter:on
    }

}
