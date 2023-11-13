package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.dto.CategoryBookCountDTO;
import com.switix.onlinebookstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findAllByNameLikeIgnoreCase(String category);

    @Query("SELECT new com.switix.onlinebookstore.dto.CategoryBookCountDTO(c.id, c.name, COUNT(b)) FROM Category c LEFT JOIN c.categoryBooks b GROUP BY c.id, c.name ORDER BY c.name ASC")
    List<CategoryBookCountDTO> countBooksByCategory();
}
