package com.matsa.services;

import com.matsa.exceptions.NotFoundException;
import com.matsa.dto.BookDto;
import com.matsa.entity.Book;
import com.matsa.repository.BookRepository;
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
        throw new NotFoundException("Book not found");
    }

    public Page<Book> getAll(String query, Pageable pageable) {
        if(query != null){
            return bookRepository.findByQuery("%" + query.toLowerCase() + "%",pageable);
        }
        return bookRepository.findAll(pageable);
    }

    public Book create(BookDto dto) {
        Book book = new Book(dto);
        return bookRepository.save(book);
    }

    public Book update(Long id, BookDto dto) {
        Book original = get(id);
        original.setName(dto.getName());
        original.setAuthor(dto.getAuthor());
        original.setDescription(dto.getDescription());
        original.setIsbn(dto.getIsbn());
        original.setPublisher(dto.getPublisher());
        original.setYear(dto.getYear());
        return bookRepository.save(original);
    }

    public void delete(Long id) {
        Book book = get(id);
        bookRepository.delete(book);
    }
}
