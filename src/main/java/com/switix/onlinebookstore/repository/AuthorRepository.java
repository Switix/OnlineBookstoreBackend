package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.dto.AuthorBookCountDto;
import com.switix.onlinebookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    List<Author> findByNameLikeIgnoreCase(String name);

    @Query("SELECT new com.switix.onlinebookstore.dto.AuthorBookCountDto(a.id, a.name, COUNT(b)) FROM Author a JOIN a.authorBooks b GROUP BY a.id, a.name ORDER BY a.name ASC")
    List<AuthorBookCountDto> countBooksByAuthorOrderedByName();

}
