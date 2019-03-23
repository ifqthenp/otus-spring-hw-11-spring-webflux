package com.otus.hw_11.repositories;

import com.otus.hw_11.domain.Author;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface AuthorRepository extends ReactiveMongoRepository<Author, String> {

    Flux<Author> findAuthorsByFirstNameContainingIgnoreCase(String firstName);

}
