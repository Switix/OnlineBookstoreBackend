package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {
    List<Book> findAllByIsRemoved(boolean isRemoved);
    List<Book> findAllByCategory_IdAndIsRemoved(Long categoryId, boolean isRemoved);
    List<Book> findAllByBookAuthors_IdAndIsRemoved(Long authorId, boolean isRemoved);
    List<Book> findAllByTitleIsLikeIgnoreCaseAndIsRemoved(String title, boolean isRemoved);

}
