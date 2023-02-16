package com.matsa.entity;

import com.matsa.dto.BookDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Book {
    @Id
    @SequenceGenerator(name = "books", sequenceName = "books")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "books")
    private Long id;
    private String name;
    private String author;
    private String description;
    private String publisher;
    private String isbn;
    private Integer year;


    public Book(String name, String author, String description, String publisher, String isbn, Integer year) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.publisher = publisher;
        this.isbn = isbn;
        this.year = year;
    }

    public Book(BookDto dto) {
        name = dto.getName();
        author = dto.getAuthor();
        description = dto.getDescription();
        publisher = dto.getPublisher();
        isbn = dto.getIsbn();
        year = dto.getYear();

    }
}
