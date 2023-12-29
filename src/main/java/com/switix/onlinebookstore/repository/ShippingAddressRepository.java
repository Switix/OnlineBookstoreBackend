package com.switix.onlinebookstore.repository;

import com.switix.onlinebookstore.model.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {
}
