package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.OrderDetailCreationDto;
import com.switix.onlinebookstore.dto.OrderDetailDto;
import com.switix.onlinebookstore.dto.OrderItemDto;
import com.switix.onlinebookstore.model.OrderDetail;

import java.util.List;

public interface OrderService {
    List<OrderItemDto> getOrderItems(Long orderDetailsId);
    List<OrderDetailDto> getOrdersDetail(Long appUserId);
    OrderDetailDto getOrderDetail(Long orderDetailsId);
    OrderDetail createOrderDetail(OrderDetailCreationDto orderDetailCreationDto,Long shoppingSessionId);
}
