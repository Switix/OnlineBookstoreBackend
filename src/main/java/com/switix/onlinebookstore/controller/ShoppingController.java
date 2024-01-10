package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.CartItemDto;
import com.switix.onlinebookstore.dto.CartItemRequestDto;
import com.switix.onlinebookstore.exception.BookNotFoundException;
import com.switix.onlinebookstore.exception.CartItemNotFoundException;
import com.switix.onlinebookstore.exception.ShoppingSessionNotFoundException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.CartItem;
import com.switix.onlinebookstore.service.ShoppingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/shopping")
public class ShoppingController {

    private final ShoppingService shoppingService;

    public ShoppingController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, BigDecimal>> getShoppingCartTotal(Authentication authentication) {
        Long shoppingSessionId = ((AppUser) authentication.getPrincipal()).getShoppingSession().getId();
        BigDecimal total = shoppingService.getShoppingCartTotal(shoppingSessionId);

        Map<String, BigDecimal> response = new HashMap<>();
        response.put("total", total);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cartItems")
    List<CartItemDto> getCartItems(Authentication authentication) {
        try {
            Long shoppingSessionId = ((AppUser) authentication.getPrincipal()).getShoppingSession().getId();
            return shoppingService.getCartItems(shoppingSessionId);
        } catch (ShoppingSessionNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/cartItems")
    public ResponseEntity<Void> addCartItem(@RequestBody CartItemRequestDto cartItemRequestDto, Authentication authentication) {
        try {
            Long shoppingSessionId = ((AppUser) authentication.getPrincipal()).getShoppingSession().getId();
            CartItem addedCartItem = shoppingService.addCartItem(cartItemRequestDto, shoppingSessionId);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{cartItemId}").buildAndExpand(addedCartItem.getId()).toUri();
            return ResponseEntity.created(location).build();
        } catch (BookNotFoundException | ShoppingSessionNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("/cartItems/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartItemId) {
        try {
            shoppingService.deleteCartItem(cartItemId);
            return ResponseEntity.noContent().build();
        } catch (CartItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping("/cartItems/{cartItemId}")
    public ResponseEntity<Void> patchCartItem(@PathVariable Long cartItemId, @RequestBody CartItemRequestDto cartItemRequestDto) {
        try {
            shoppingService.updateCartItem(cartItemId, cartItemRequestDto);
            return ResponseEntity.noContent().build();
        } catch (CartItemNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }
}
