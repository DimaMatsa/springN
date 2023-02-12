package com.matsa;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class BookService {
    public Book get(Long id) {
        //get book by id from DB
        Book testBook = new  Book("Incognito", "Buben", "pro Bubna", "Selo", "", 2000);
        Book testBook2 = new  Book("Incognito2", "Buben2", "pro Bubna2", "Selo2", "", 2001);

        return new Book();
    }

    public List<Book> getAll() {
        //get all books from DB
        Book testBook = new  Book("Incognito", "Buben", "pro Bubna", "Selo", "", 2000);
        Book testBook2 = new  Book("Incognito2", "Buben2", "pro Bubna2", "Selo2", "", 2001);
        return List.of(testBook,testBook2);
    }

    public Book create(Book book) {
        //save book to DB
        return book;
    }

    public Book update(Long id, Book book) {
        //get book by id and update it
        return book;
    }

    public void delete(Long id) {
        //delete book by id
    }
}
