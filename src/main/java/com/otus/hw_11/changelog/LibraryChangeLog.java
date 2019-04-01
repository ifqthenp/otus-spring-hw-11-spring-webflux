package com.otus.hw_11.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.MongoClient;
import com.otus.hw_11.domain.Author;
import com.otus.hw_11.domain.Comment;
import com.otus.hw_11.domain.Genre;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Slf4j
@ChangeLog
public class LibraryChangeLog {

    private static final String BOOKS = "books";
    private static final String BOOK_CLASS = "com.otus.hw_11.domain.Book";
    private static final String LIBRARY_DB = "library";

    private static CodecRegistry codecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    @ChangeSet(author = "user", id = "dropDb", order = "001", runAlways = true)
    public void dropDb(final MongoTemplate template) {
        template.getDb().drop();
        log.info("Dropped db: {}", LIBRARY_DB);
    }

    @ChangeSet(author = "admin", id = "createInitialBooks", order = "030")
    public void initializeBooksCollection(final MongoTemplate template) {
        final var collection = template.getDb()
            .withCodecRegistry(codecRegistry).getCollection(BOOKS);
        final var books = new ArrayList<Document>() {{
            add(new Document()
                .append("_id", new ObjectId("5c857854402f51169241931f"))
                .append("title", "Alice in Wonderland")
                .append("year", "1865")
                .append("authors", singletonList(
                    new Author("Lewis", "Carrol")))
                .append("genres", Arrays.asList(
                    new Genre("Children's Literature"),
                    new Genre("Fantasy")))
                .append("comments", Arrays.asList(
                    new Comment(1, "excellent"), new Comment(2, "awesome")))
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419320"))
                .append("title", "Jane Eyre")
                .append("year", "1847")
                .append("authors", singletonList(
                    new Author("Charlotte", "Bronte")))
                .append("genres", Arrays.asList(
                    new Genre("Novel"),
                    new Genre("Autobiography")))
                .append("comments", singletonList(
                    new Comment(1, "enjoyable reading")))
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419321"))
                .append("title", "Don Quixote")
                .append("year", "1615")
                .append("authors", singletonList(
                    new Author("Miguel", "de Cervantes")))
                .append("genres", singletonList(
                    new Genre("Novel")))
                .append("comments", singletonList(
                    new Comment(1, "classics")))
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419322"))
                .append("title", "The Time Machine")
                .append("year", "1895")
                .append("authors", singletonList(
                    new Author("Herbert", "Wells")))
                .append("genres", singletonList(
                    new Genre("Science-Fiction")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419323"))
                .append("title", "Anna Karenina")
                .append("year", "1878")
                .append("authors", singletonList(
                    new Author("Leo", "Tolstoy")))
                .append("genres", Arrays.asList(
                    new Genre("Literary Realism"),
                    new Genre("Novel")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419324"))
                .append("title", "Pride and Prejudice")
                .append("year", "1813")
                .append("authors", singletonList(
                    new Author("Jane", "Austen")))
                .append("genres", Arrays.asList(
                    new Genre("Romance"),
                    new Genre("Novel")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419325"))
                .append("title", "Childhood")
                .append("year", "1852")
                .append("authors", singletonList(
                    new Author("Leo", "Tolstoy")))
                .append("genres", Arrays.asList(
                    new Genre("Autobiography"),
                    new Genre("Literary Realism"),
                    new Genre("Novel")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419326"))
                .append("title", "Boyhood")
                .append("year", "1854")
                .append("authors", singletonList(
                    new Author("Leo", "Tolstoy")))
                .append("genres", Arrays.asList(
                    new Genre("Autobiography"),
                    new Genre("Literary Realism"),
                    new Genre("Novel")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419327"))
                .append("title", "Love in the Time of Cholera")
                .append("year", "1985")
                .append("authors", singletonList(
                    new Author("Gabriel", "García Márquez")))
                .append("genres", singletonList(
                    new Genre("Novel")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419328"))
                .append("title", "The Book of Calculation")
                .append("year", "1202")
                .append("authors", singletonList(
                    new Author("Leonardo", "Fibonacci")))
                .append("genres", singletonList(
                    new Genre("Mathematics")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f511692419329"))
                .append("title", "The Twelve Chairs")
                .append("year", "1928")
                .append("authors", Arrays.asList(
                    new Author("Ilya", "Ilf"),
                    new Author("Yevgeni", "Petrov")))
                .append("genres", Arrays.asList(
                    new Genre("Satire"),
                    new Genre("Novel")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
            add(new Document()
                .append("_id", new ObjectId("5c857854402f51169241932a"))
                .append("title", "The Little Golden Calf")
                .append("year", "1931")
                .append("authors", Arrays.asList(
                    new Author("Ilya", "Ilf"),
                    new Author("Yevgeni", "Petrov")))
                .append("genres", Arrays.asList(
                    new Genre("Satire"),
                    new Genre("Novel")))
                .append("comments", new ArrayList<>())
                .append("_class", BOOK_CLASS));
        }};

        collection.insertMany(books);
        log.info("Created {} documents in '{}'", books.size(), BOOKS);
    }

}
