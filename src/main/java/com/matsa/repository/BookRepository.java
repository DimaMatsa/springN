package com.matsa.repository;

import com.matsa.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select book from Book book where lower(book.name) like :query " +
            "or lower(book.author) like :query " +
            "or lower(book.description) like :query " +
            "or lower(book.isbn) like :query ")
    Page<Book> findByQuery(String query, Pageable pageable);

}
