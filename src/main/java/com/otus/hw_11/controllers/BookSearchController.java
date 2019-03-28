package com.otus.hw_11.controllers;

import com.otus.hw_11.dto.BookSearchResultDto;
import com.otus.hw_11.services.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookSearchController {

    private final BookService bookService;

    @GetMapping("/library/books/search")
    public Mono<String> fullSearchForm() {
        return Mono.just("book_search_form");
    }

    @GetMapping("/library/books/search/author")
    public Mono<String> searchByAuthor(@RequestParam(required = false) final String lastName, final Model model) {
        if (!lastName.isBlank()) {
            Flux<BookSearchResultDto> books = bookService.findBooksByAuthorRequestParam(lastName);
            model.addAttribute("books", new ReactiveDataDriverContextVariable(books, 1));
        }
        return Mono.just("book_search_result");
    }

    @GetMapping("/library/books/search/title")
    public Mono<String> searchByTitle(@RequestParam(required = false) final String title, final Model model) {
        if (!title.isBlank()) {
            Flux<BookSearchResultDto> books = bookService.findBooksByTitleRequestParam(title);
            model.addAttribute("books", new ReactiveDataDriverContextVariable(books, 1000));
        }
        return Mono.just("book_search_result");
    }

}
