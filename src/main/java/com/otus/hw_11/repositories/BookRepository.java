package com.otus.hw_11.repositories;

import com.otus.hw_11.domain.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Flux<Book> findBooksByTitleContainingIgnoreCase(String text);

}
