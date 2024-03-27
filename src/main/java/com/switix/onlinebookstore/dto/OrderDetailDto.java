package com.switix.onlinebookstore.dto;

import com.switix.onlinebookstore.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private Long id;
    private BigDecimal total;
    private Instant createdAt;
    private OrderStatus orderStatus;
}
