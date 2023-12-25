package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.AppUserDto;

import com.switix.onlinebookstore.dto.UpdateAppUserProfileDto;
import com.switix.onlinebookstore.exception.InvalidPasswordException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

        if (authentication instanceof AnonymousAuthenticationToken || authentication ==null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "User is not authorized");
        }
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
}
