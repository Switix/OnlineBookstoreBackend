package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.OrderDetailCreationDto;
import com.switix.onlinebookstore.dto.OrderDetailDto;
import com.switix.onlinebookstore.dto.OrderItemDto;
import com.switix.onlinebookstore.exception.BookInsufficientStockException;
import com.switix.onlinebookstore.exception.EmptyShoppingCartException;
import com.switix.onlinebookstore.exception.OrderDetailNotFoundException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.OrderDetail;
import com.switix.onlinebookstore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("api/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<OrderDetailDto> getOrderDetail(@PathVariable Long orderDetailId) {
        try {
            return ResponseEntity.ok(orderService.getOrderDetail(orderDetailId));
        } catch (OrderDetailNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @GetMapping()
    public List<OrderDetailDto> getOrderDetails(Authentication authentication){
        Long appUserId = ((AppUser) authentication.getPrincipal()).getId();
        return orderService.getOrdersDetail(appUserId);
    }
    @GetMapping("/{orderDetailId}/orderItems")
        public List<OrderItemDto> getOrderItems(@PathVariable Long orderDetailId){
        return orderService.getOrderItems(orderDetailId);
    }
    @PostMapping()
    public ResponseEntity<Void> createOrderDetail(@RequestBody OrderDetailCreationDto orderDetailCreationDto, Authentication authentication){
        try {
            Long shoppingSessionId = ((AppUser) authentication.getPrincipal()).getShoppingSession().getId();
            OrderDetail orderDetail = orderService.createOrderDetail(orderDetailCreationDto,shoppingSessionId);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{orderDetailId}").buildAndExpand(orderDetail.getId()).toUri();
            return ResponseEntity.created(location).build();
        }
        catch (EmptyShoppingCartException e){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage(), e);
        }
        catch (BookInsufficientStockException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

}
