package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findAllByAppUser_Id(Long appUserId);
}
