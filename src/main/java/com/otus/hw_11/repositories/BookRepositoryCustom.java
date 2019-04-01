package com.otus.hw_11.repositories;

import reactor.core.publisher.Flux;

public interface BookRepositoryCustom {

    Flux<String> findDistinctAuthorLastName();

    Flux<String> findDistinctGenres();

}
