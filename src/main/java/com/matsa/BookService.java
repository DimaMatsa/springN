package com.matsa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {
    @Autowired
    public BookRepository bookRepository;
    public Book get(Long id) {
        Optional<Book> optional = bookRepository.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        throw new RuntimeException("Book not found");
    }

    public Page<Book> getAll(String query, Pageable pageable) {
        if(query != null){
            return bookRepository.findByQuery("%" + query.toLowerCase() + "%",pageable);
        }
        return bookRepository.findAll(pageable);
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public Book update(Long id, Book book) {
        Book original = get(id);
        original.setName(book.getName());
        original.setAuthor(book.getAuthor());
        original.setDescription(book.getDescription());
        original.setIsbn(book.getIsbn());
        original.setPublisher(book.getPublisher());
        original.setYear(book.getYear());
        return bookRepository.save(original);
    }

    public void delete(Long id) {
        Book book = get(id);
        bookRepository.delete(book);
    }
}
