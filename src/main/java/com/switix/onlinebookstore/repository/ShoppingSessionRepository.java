package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.ShoppingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingSessionRepository extends JpaRepository<ShoppingSession, Long> {
}
