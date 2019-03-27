package com.otus.hw_11.dto;

import com.otus.hw_11.domain.Author;
import com.otus.hw_11.domain.Genre;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class BookSearchResultDto {

    String title;
    List<Author> authors;
    List<Genre> genres;

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors.stream()
            .map(a -> a.getFirstName() + " " + a.getLastName())
            .collect(Collectors.joining(", "));
    }

    public String getGenres() {
        return genres.stream()
            .map(Genre::getGenreName)
            .collect(Collectors.joining(", "));
    }

}
