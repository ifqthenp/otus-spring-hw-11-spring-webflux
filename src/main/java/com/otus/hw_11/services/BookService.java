package com.otus.hw_11.services;

import com.otus.hw_11.dto.BookSearchResultDto;
import com.otus.hw_11.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;

    public Mono<Long> getAuthorsCount() {
        return bookRepo.findDistinctAuthorLastName().count();
    }

    public Mono<Long> getGenresCount() {
        return bookRepo.findDistinctGenres().count();
    }

    public Mono<Long> getBooksCount() {
        return bookRepo.count();
    }

    public Flux<BookSearchResultDto> findBooksByTitleRequestParam(final String title) {
        return bookRepo.findBooksByTitleContainingIgnoreCase(title);
    }

}
