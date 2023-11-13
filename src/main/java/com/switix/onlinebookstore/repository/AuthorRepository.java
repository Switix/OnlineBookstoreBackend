package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    List<Author> findByNameLikeIgnoreCase(String name);
}
