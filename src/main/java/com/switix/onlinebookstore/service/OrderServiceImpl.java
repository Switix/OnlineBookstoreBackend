package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.*;
import com.switix.onlinebookstore.exception.*;
import com.switix.onlinebookstore.model.*;
import com.switix.onlinebookstore.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingService shoppingService;
    private final ShoppingSessionRepository shoppingSessionRepository;
    private final ShippingAddressService shippingAddressService;
    private final BillingAddressService billingAddressService;
    private final PayMethodRepository payMethodRepository;
    private final ShipmentMethodRepository shipmentMethodRepository;
    private final OrderStatusRepository orderStatusRepository;

    public OrderServiceImpl(OrderDetailRepository orderDetailRepository, OrderItemRepository orderItemRepository, ShoppingService shoppingService, ShoppingSessionRepository shoppingSessionRepository, ShippingAddressService shippingAddressService, BillingAddressService billingAddressService, PayMethodRepository payMethodRepository, ShipmentMethodRepository shipmentMethodRepository, OrderStatusRepository orderStatusRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderItemRepository = orderItemRepository;
        this.shoppingService = shoppingService;
        this.shoppingSessionRepository = shoppingSessionRepository;
        this.shippingAddressService = shippingAddressService;
        this.billingAddressService = billingAddressService;
        this.payMethodRepository = payMethodRepository;
        this.shipmentMethodRepository = shipmentMethodRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderDetailsId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderDetail_Id(orderDetailsId);
        return orderItems.stream()
                .map(this::mapToOrderItemDto)
                .toList();
    }

    @Override
    public List<OrderDetailDto> getOrdersDetail(Long appUserId) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByAppUser_Id(appUserId);
        return orderDetails.stream()
                .map(this::mapToOrderDetailDto)
                .toList();

    }

    @Override
    public List<OrderDetailDto> getOrdersDetailAdmin() {
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        return orderDetails.stream()
                .map(this::mapToOrderDetailDto)
                .toList();
    }

    @Override
    public OrderDetail getOrderDetail(Long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new OrderDetailNotFoundException("Order detail not found"));
    }

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailCreationDto orderDetailCreationDto, Long shoppingSessionId) {
        ShoppingSession shoppingSession = shoppingSessionRepository.findById(shoppingSessionId)
                .orElseThrow(() -> new ShoppingSessionNotFoundException("Shopping session not found"));


        BillingAddress billingAddress = billingAddressService.getBillingAddress(orderDetailCreationDto.getBillingAddressId());
        ShippingAddress shippingAddress = shippingAddressService.getShippingAddress(orderDetailCreationDto.getShippingAddressId());

        PayMethod payMethod = payMethodRepository.findById(orderDetailCreationDto.getPayMethodId()).get();
        ShipmentMethod shipmentMethod = shipmentMethodRepository.findById(orderDetailCreationDto.getShipmentMethodId()).get();

        // check if the shopping cart is empty (no items no order)
        List<CartItemDto> cartItems = shoppingService.getCartItems(shoppingSessionId);
        if (cartItems.isEmpty()) {
            throw new EmptyShoppingCartException("Shopping cart is empty");
        }

        //check book stock
        cartItems.forEach(cartItem -> {
            Book cartItemBook = cartItem.getBook();
            if (cartItemBook.getInventory().getQuantity() < cartItem.getQuantity()) {
                throw new BookInsufficientStockException(String.format("Requested quantity (%d) exceeds available stock (%d) for book with sId %d",
                        cartItem.getQuantity(),
                        cartItemBook.getInventory().getQuantity(),
                        cartItemBook.getId()));
            }

            //update inventory quantity
            cartItemBook.getInventory().setQuantity(cartItemBook.getInventory().getQuantity() - cartItem.getQuantity());
        });

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setTotal(shoppingSession.getTotal().add(shipmentMethod.getPrice()));
        orderDetail.setAppUser(shoppingSession.getAppUser());
        orderDetail.setShippingAddress(shippingAddress);
        orderDetail.setBillingAddress(billingAddress);
        orderDetail.setOrderStatus(orderStatusRepository.findById(2L).get());
        orderDetail.setPayMethod(payMethod);
        orderDetail.setShipmentMethod(shipmentMethod);
        List<OrderItem> orderItems = cartItems.stream()
                .map(this::mapToOrderItem)
                .toList();


        orderItems.forEach(orderItem -> orderItem.setOrderDetail(orderDetail));

        //clear shopping cart
        List<Long> cartItemIds = cartItems.stream()
                .map(CartItemDto::getId)
                .toList();
        shoppingService.deleteAllCartItems(cartItemIds);
        shoppingSession.setTotal(BigDecimal.ZERO);
        shoppingSessionRepository.save(shoppingSession);

        OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
        orderItemRepository.saveAll(orderItems);
        return savedOrderDetail;
    }

    @Override
    public void updateOrder(UpdateOrderDto updateOrderDto) {
        OrderDetail orderDetail = orderDetailRepository.findById(updateOrderDto.getId())
                .orElseThrow(() -> new OrderDetailNotFoundException("Order detail not found"));

        OrderStatus orderStatus = orderStatusRepository.findById(updateOrderDto.getOrderStatusId())
                .orElseThrow(() -> new OrderStatusNotFoundException("Order Status not found"));

        orderDetail.setOrderStatus(orderStatus);

        orderStatusRepository.save(orderStatus);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderDetailRepository.deleteById(orderId);
    }


    private OrderItem mapToOrderItem(CartItemDto cartItemDto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(cartItemDto.getId());
        orderItem.setBook(cartItemDto.getBook());
        orderItem.setQuantity(cartItemDto.getQuantity());
        return orderItem;
    }

    private OrderDetailDto mapToOrderDetailDto(OrderDetail orderDetail) {
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setId(orderDetail.getId());
        orderDetailDto.setTotal(orderDetail.getTotal());
        orderDetailDto.setCreatedAt(orderDetail.getCreatedAt());
        orderDetailDto.setOrderStatus(orderDetail.getOrderStatus());
        return orderDetailDto;
    }

    private OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(orderItem.getId());
        orderItemDto.setBook(orderItem.getBook());
        orderItemDto.setQuantity(orderItem.getQuantity());

        return orderItemDto;
    }
}
