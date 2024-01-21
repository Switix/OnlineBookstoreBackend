package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.CartItemDto;
import com.switix.onlinebookstore.dto.CartItemRequestDto;
import com.switix.onlinebookstore.model.CartItem;

import java.math.BigDecimal;
import java.util.List;

public interface ShoppingService {
    List<CartItemDto> getCartItems(Long shoppingSessionId);
    CartItem addCartItem(CartItemRequestDto cartItemRequestDto, Long shoppingSessionId);
    void updateCartItem(Long cartItemId,CartItemRequestDto cartItemRequestDto);
    void deleteCartItem(Long cartItemId);
    BigDecimal getShoppingCartTotal(Long shoppingSessionId);
    void deleteAllCartItems(List<Long> cartItemIds);
}
