package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrderDetail_Id(Long orderDetailsId);
}
