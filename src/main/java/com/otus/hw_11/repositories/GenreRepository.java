package com.otus.hw_11.repositories;

import com.otus.hw_11.domain.Genre;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    @Query("{'genreName': { $regex: ?0, $options: 'i' }}")
    Flux<Genre> findGenresByName(String genre);

}
