package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.ShippingAddressDto;
import com.switix.onlinebookstore.exception.CityNotFoundException;
import com.switix.onlinebookstore.exception.CountryNotFoundException;
import com.switix.onlinebookstore.exception.ShippingAddressNotFoundException;
import com.switix.onlinebookstore.exception.UnauthorizedAccessException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.ShippingAddress;
import com.switix.onlinebookstore.service.ShippingAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/shippingAddress")
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;

    public ShippingAddressController(ShippingAddressService shippingAddressService) {

        this.shippingAddressService = shippingAddressService;
    }

    @PostMapping()
    ResponseEntity<ShippingAddress> createShippingAddress(Authentication authentication, @RequestBody ShippingAddressDto shippingAddressDto) {
        try{
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();
            ShippingAddress shippingAddress = shippingAddressService.createShippingAddress(authenticatedUser, shippingAddressDto);
            return ResponseEntity.ok(shippingAddress);
        }
         catch (CountryNotFoundException | CityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping()
    ResponseEntity<ShippingAddress> updateShippingAddress(Authentication authentication, @RequestBody ShippingAddressDto shippingAddressDto) {
        try {
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();
            ShippingAddress shippingAddress = shippingAddressService.updateShippingAddress(authenticatedUser, shippingAddressDto);
            return ResponseEntity.ok(shippingAddress);
        }
        catch (CountryNotFoundException | CityNotFoundException | ShippingAddressNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (UnauthorizedAccessException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @DeleteMapping("/{shippingAddressId}")
    ResponseEntity<Void> deleteShippingAddress(Authentication authentication, @PathVariable Long shippingAddressId) {
        try {
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();
            shippingAddressService.unbindShippingAddress(authenticatedUser, shippingAddressId);
            return ResponseEntity.noContent().build();
        }
        catch (ShippingAddressNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (UnauthorizedAccessException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
