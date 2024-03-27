package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus,Long> {
}
