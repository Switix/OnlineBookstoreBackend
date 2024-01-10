package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.CartItemDto;
import com.switix.onlinebookstore.dto.CartItemRequestDto;
import com.switix.onlinebookstore.exception.BookNotFoundException;
import com.switix.onlinebookstore.exception.CartItemNotFoundException;
import com.switix.onlinebookstore.exception.ShoppingSessionNotFoundException;
import com.switix.onlinebookstore.model.Book;
import com.switix.onlinebookstore.model.CartItem;
import com.switix.onlinebookstore.model.ShoppingSession;
import com.switix.onlinebookstore.repository.BookRepository;
import com.switix.onlinebookstore.repository.CartItemRepository;
import com.switix.onlinebookstore.repository.ShoppingSessionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingServiceImpl implements ShoppingService {


    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingSessionRepository shoppingSessionRepository;

    public ShoppingServiceImpl(CartItemRepository cartItemRepository, BookRepository bookRepository, ShoppingSessionRepository shoppingSessionRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.shoppingSessionRepository = shoppingSessionRepository;
    }


    @Override
    public List<CartItemDto> getCartItems(Long shoppingSessionId) {
        List<CartItem> cartItems = cartItemRepository.findAllByShoppingSession_IdOrderById(shoppingSessionId);

        return cartItems.stream()
                .map(this::mapToCartItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CartItem addCartItem(CartItemRequestDto cartItemRequestDto, Long shoppingSessionId) {

        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        ShoppingSession shoppingSession = shoppingSessionRepository.findById(shoppingSessionId)
                .orElseThrow(() -> new ShoppingSessionNotFoundException("Shopping session not found"));

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingSession(shoppingSession);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        calculateShoppingCartTotal(shoppingSession);

        return savedCartItem;
    }

    @Override
    public void updateCartItem(Long cartItemId, CartItemRequestDto cartItemRequestDto) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItemRepository.save(cartItem);

        calculateShoppingCartTotal(cartItem.getShoppingSession());
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);

        calculateShoppingCartTotal(cartItem.getShoppingSession());
    }

    @Override
    public BigDecimal getShoppingCartTotal(Long shoppingSessionId) {
        return shoppingSessionRepository.findById(shoppingSessionId)
                .orElseThrow(() -> new ShoppingSessionNotFoundException("Shopping session not found"))
                .getTotal();
    }

    private CartItemDto mapToCartItemDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setBook(cartItem.getBook());
        cartItemDto.setQuantity(cartItem.getQuantity());

        return cartItemDto;
    }

    private void calculateShoppingCartTotal(ShoppingSession shoppingSession) {

        List<CartItem> cartItems = cartItemRepository.findAllByShoppingSession_IdOrderById(shoppingSession.getId());

        BigDecimal total = cartItems.stream()
                .map(cartItem -> cartItem.getBook().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        shoppingSession.setTotal(total);
        shoppingSessionRepository.save(shoppingSession);
    }
}
