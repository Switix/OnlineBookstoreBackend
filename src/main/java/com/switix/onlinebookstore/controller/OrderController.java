package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.OrderDetailCreationDto;
import com.switix.onlinebookstore.dto.OrderDetailDto;
import com.switix.onlinebookstore.dto.OrderItemDto;
import com.switix.onlinebookstore.dto.UpdateOrderDto;
import com.switix.onlinebookstore.exception.BookInsufficientStockException;
import com.switix.onlinebookstore.exception.EmptyShoppingCartException;
import com.switix.onlinebookstore.exception.OrderDetailNotFoundException;
import com.switix.onlinebookstore.exception.OrderStatusNotFoundException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.OrderDetail;
import com.switix.onlinebookstore.model.PayMethod;
import com.switix.onlinebookstore.model.ShipmentMethod;
import com.switix.onlinebookstore.repository.PayMethodRepository;
import com.switix.onlinebookstore.repository.ShipmentMethodRepository;
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
    private final PayMethodRepository payMethodRepository;
    private final ShipmentMethodRepository shipmentMethodRepository;

    public OrderController(OrderService orderService, PayMethodRepository payMethodRepository, ShipmentMethodRepository shipmentMethodRepository) {
        this.orderService = orderService;
        this.payMethodRepository = payMethodRepository;
        this.shipmentMethodRepository = shipmentMethodRepository;
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<OrderDetail> getOrderDetail(@PathVariable Long orderDetailId) {
        try {
            return ResponseEntity.ok(orderService.getOrderDetail(orderDetailId));
        } catch (OrderDetailNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping()
    public List<OrderDetailDto> getOrderDetails(Authentication authentication) {
        Long appUserId = ((AppUser) authentication.getPrincipal()).getId();
        return orderService.getOrdersDetail(appUserId);
    }

    @GetMapping("admin")
    public List<OrderDetailDto> getOrderDetailsAdmin() {
        return orderService.getOrdersDetailAdmin();
    }

    @GetMapping("/{orderDetailId}/orderItems")
    public List<OrderItemDto> getOrderItems(@PathVariable Long orderDetailId) {
        return orderService.getOrderItems(orderDetailId);
    }

    @PostMapping()
    public ResponseEntity<Void> createOrderDetail(@RequestBody OrderDetailCreationDto orderDetailCreationDto, Authentication authentication) {
        try {
            Long shoppingSessionId = ((AppUser) authentication.getPrincipal()).getShoppingSession().getId();
            OrderDetail orderDetail = orderService.createOrderDetail(orderDetailCreationDto, shoppingSessionId);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{orderDetailId}").buildAndExpand(orderDetail.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (EmptyShoppingCartException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage(), e);
        } catch (BookInsufficientStockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    @PatchMapping("admin")
    public ResponseEntity<Void> updateOrder(@RequestBody UpdateOrderDto updateOrderDto) {
        try {
            orderService.updateOrder(updateOrderDto);
            return ResponseEntity.noContent().build();
        } catch (OrderStatusNotFoundException | OrderDetailNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    @DeleteMapping("admin/{orderId}")
    public ResponseEntity<Void> deleteOrder( @PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.noContent().build();
        } catch (OrderStatusNotFoundException | OrderDetailNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/payMethods")
    public List<PayMethod> getPayMethods() {
        return payMethodRepository.findAll();
    }

    @GetMapping("/shipmentMethods")
    public List<ShipmentMethod> getShipmentMethods() {
        return shipmentMethodRepository.findAll();
    }

}
