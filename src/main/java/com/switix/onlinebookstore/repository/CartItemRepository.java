package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findAllByShoppingSession_IdOrderById(Long shoppingSessionId);
}
