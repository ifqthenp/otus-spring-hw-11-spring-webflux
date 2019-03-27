package com.otus.hw_11.controllers;

import com.otus.hw_11.dto.BookSearchResultDto;
import com.otus.hw_11.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

@Controller
@RequiredArgsConstructor
public class BookSearchController {

    private final BookService bookService;

    @GetMapping("/library/books/search")
    public String fullSearchForm() {
        return "book_search_form";
    }

    @GetMapping("/library/books/search/title")
    public String quickSearchForm(@RequestParam(required = false) final String title, final Model model) {
        if (!title.isBlank()) {
            Flux<BookSearchResultDto> books = bookService.findBooksByTitleRequestParam(title);
            model.addAttribute("books", new ReactiveDataDriverContextVariable(books, 1000));
        }
        return "book_search_result";
    }
}
