package com.otus.hw_11.dto;

import com.otus.hw_11.domain.Author;
import com.otus.hw_11.domain.Book;
import com.otus.hw_11.domain.Genre;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.HashSet;

@Data
public class BookAddFormDto {

    @NotBlank
    private String title;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String genre;

    @NotBlank
    @Pattern(
        regexp = "\\d{4}",
        message = "Invalid year format. Must be 4 digits"
    )
    private String year;

    public Book toEntity(final BookAddFormDto bookAddFormDto) {
        final Author author = new Author();
        author.setFirstName(bookAddFormDto.getFirstName());
        author.setLastName(bookAddFormDto.getLastName());

        final Genre genre = new Genre();
        genre.setGenreName(bookAddFormDto.getGenre());

        final Book book = new Book();
        book.setTitle(bookAddFormDto.getTitle());
        book.setYear(bookAddFormDto.getYear());
        book.setAuthors(new ArrayList<>() {{
            add(author);
        }});
        book.setGenres(new HashSet<>() {{
            add(genre);
        }});
        return book;
    }

}
