package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.AppUserChangePasswordDto;
import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.ChangeBillingAddressDto;
import com.switix.onlinebookstore.dto.UpdateAppUserProfileDto;
import com.switix.onlinebookstore.exception.CityNotFoundException;
import com.switix.onlinebookstore.exception.CountryNotFoundException;
import com.switix.onlinebookstore.exception.InvalidPasswordException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.BillingAddress;
import com.switix.onlinebookstore.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/user")
public class AppUserController {

    private final AppUserService userService;

    public AppUserController(AppUserService userService) {
        this.userService = userService;
    }


    @PatchMapping("/profile")
    public ResponseEntity<AppUserDto> updateProfile(Authentication authentication,
                                                    @RequestBody UpdateAppUserProfileDto updateAppUserProfileDto) {

        try {
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();

            AppUserDto updatedUser = userService.updateUser(authenticatedUser, updateAppUserProfileDto);
            return ResponseEntity.ok(updatedUser);

        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Password is incorrect", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Server error", e);
        }
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<Void> ChangePassword(Authentication authentication,
                                               @RequestBody AppUserChangePasswordDto appUserChangePasswordDto) {
        try {
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();

            userService.changeAppUserPassword(authenticatedUser, appUserChangePasswordDto);
            return ResponseEntity.noContent().build();

        } catch (InvalidPasswordException e) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Password is incorrect", e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Server error", e);
        }
    }

    @PatchMapping("/billingAddressChange")
    public ResponseEntity<BillingAddress> changeBillingAddress(Authentication authentication,
                                                               @RequestBody ChangeBillingAddressDto changeBillingAddressDto) {
        try {
            AppUser authenticatedUser = (AppUser) authentication.getPrincipal();

            BillingAddress newBillingAddress = userService.changeBillingAddress(authenticatedUser, changeBillingAddressDto);
            return ResponseEntity.ok(newBillingAddress);

        } catch (CityNotFoundException | CountryNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
