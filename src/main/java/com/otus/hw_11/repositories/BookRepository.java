package com.otus.hw_11.repositories;

import com.otus.hw_11.domain.Book;
import com.otus.hw_11.dto.BookSearchResultDto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookRepositoryCustom {

    Flux<BookSearchResultDto> findBooksByTitleContainingIgnoreCase(String text);

}
