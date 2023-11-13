package com.switix.onlinebookstore.repository;
import com.switix.onlinebookstore.model.BookInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInventoryRepository extends JpaRepository<BookInventory,Long> {
}
