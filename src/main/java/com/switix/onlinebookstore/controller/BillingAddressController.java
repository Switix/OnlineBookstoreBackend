package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.BillingAddressDto;
import com.switix.onlinebookstore.exception.BillingAddressNotFoundException;
import com.switix.onlinebookstore.exception.CityNotFoundException;
import com.switix.onlinebookstore.exception.CountryNotFoundException;
import com.switix.onlinebookstore.exception.UnauthorizedAccessException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.BillingAddress;
import com.switix.onlinebookstore.service.BillingAddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/billingAddress/")
public class BillingAddressController {

    private final BillingAddressService billingAddressService;

    public BillingAddressController(BillingAddressService billingAddressService) {
        this.billingAddressService = billingAddressService;
    }

    @PostMapping()
    ResponseEntity<BillingAddress> createBillingAddress(Authentication authentication, @RequestBody BillingAddressDto billingAddressDto) {
        try{
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();
            BillingAddress billingAddress = billingAddressService.createBillingAddress(authenticatedUser, billingAddressDto);
            return ResponseEntity.ok(billingAddress);
        }
         catch (CountryNotFoundException | CityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PatchMapping()
    ResponseEntity<BillingAddress> updateBillingAddress(Authentication authentication, @RequestBody BillingAddressDto billingAddressDto) {
        try {
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();
            BillingAddress billingAddress = billingAddressService.updateBillingAddress(authenticatedUser, billingAddressDto);
            return ResponseEntity.ok(billingAddress);
        }
        catch (CountryNotFoundException | CityNotFoundException | BillingAddressNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (UnauthorizedAccessException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }

    @DeleteMapping("{billingAddressId}")
    ResponseEntity<Void> deleteBillingAddress(Authentication authentication, @PathVariable Long billingAddressId) {
        try {
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();
            billingAddressService.unbindBillingAddress(authenticatedUser, billingAddressId);
            return ResponseEntity.noContent().build();
        }
        catch (BillingAddressNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        catch (UnauthorizedAccessException e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        }
    }
}
