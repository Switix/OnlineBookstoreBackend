package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.PayMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayMethodRepository extends JpaRepository<PayMethod,Long> {
}
