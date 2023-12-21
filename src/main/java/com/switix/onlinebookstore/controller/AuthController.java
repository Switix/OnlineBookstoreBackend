package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.LoginDto;
import com.switix.onlinebookstore.dto.RegisterDto;
import com.switix.onlinebookstore.exception.EmailAlreadyExistsException;
import com.switix.onlinebookstore.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<AppUserDto> register(@RequestBody RegisterDto registerDto) {
        try {
            AppUserDto appUserDto = authService.register(registerDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(appUserDto);
        }
        catch (EmailAlreadyExistsException e){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "The email address provided is already in use.", e);
        }

    }

    @PostMapping("login")
    public ResponseEntity<AppUserDto> authenticateUser(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

        try {
            AppUserDto appUser = authService.authenticateUser(loginDto.getEmail(), loginDto.getPassword(),request,response);
            return ResponseEntity.ok(appUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        authService.logoutUser(request, response,authentication);
        return ResponseEntity.noContent().build();
    }
}
