package com.otus.hw_11.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private ObjectId id;

    private String title;

    private String year;

    @DBRef(db = "library")
    private List<Author> authors = new ArrayList<>();

    @DBRef(db = "library")
    private Set<Genre> genres = new HashSet<>();

    private List<Comment> comments = new ArrayList<>();

}
