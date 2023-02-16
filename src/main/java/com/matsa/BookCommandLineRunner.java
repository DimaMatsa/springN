package com.matsa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matsa.entity.Book;
import com.matsa.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class BookCommandLineRunner implements CommandLineRunner {
    private final String BOOKS_JSON = "/json/books.json";
    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.findAll().size() == 0) {
            try {
                TypeReference<List<BookData>> typeReference = new TypeReference<List<BookData>>() {};
                InputStream inputStream = TypeReference.class.getResourceAsStream(BOOKS_JSON);
                List<BookData> books = new ObjectMapper().readValue(inputStream, typeReference);
                if (books != null && !books.isEmpty()) {
                    List<Book> bookList = new ArrayList<>();
                    books.forEach(book -> bookList.add(new Book(book.getName(), book.getAuthor(), book.getDescription(), book.getPublisher(), book.getIsbn(), book.getYear())));
                    List<Book> savedBookList = bookRepository.saveAll(bookList);
                    System.out.println(savedBookList.size());
                }
            } catch (IOException e) {
                System.out.println("Unable to save books: " + e.getMessage());
            }

        }
    }
}

