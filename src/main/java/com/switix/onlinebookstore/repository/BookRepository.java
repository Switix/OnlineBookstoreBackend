package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAllByCategory_Id(Long categoryId);
    List<Book> findAllByBookAuthors_Id(Long authorId);

}
