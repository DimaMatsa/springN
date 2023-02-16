package com.matsa.controller;

import com.matsa.dto.BookDto;
import com.matsa.entity.Book;
import com.matsa.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;
    @GetMapping("/book")
    private Page<Book> getAllBooks(@RequestParam Integer page,
                                   @RequestParam Integer size,
                                   @RequestParam(required = false) String query) {
        Pageable pageable = PageRequest.of(page, size);
        return bookService.getAll(query, pageable);
    }

    @GetMapping("/book/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.get(id);
    }

    @PostMapping("/book")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book createBook(@Valid @RequestBody BookDto dto) {
        return bookService.create(dto);
    }

    @PutMapping("/book/{id}")
    public Book editBook(@PathVariable Long id,@Valid @RequestBody BookDto dto) {
        return bookService.update(id, dto);
    }

    @DeleteMapping("/book/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}

