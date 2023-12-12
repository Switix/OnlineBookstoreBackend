package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.LoginDto;
import com.switix.onlinebookstore.dto.RegisterDto;
import com.switix.onlinebookstore.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        if(authService.register(registerDto) == null){
            return ResponseEntity.badRequest().body("Username is taken!");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registration successful");
    }

    @PostMapping("login")
    public ResponseEntity<AppUserDto> authenticateUser(@RequestBody LoginDto loginDto) {

        try {
            AppUserDto appUser = authService.authenticateUser(loginDto.getEmail(), loginDto.getPassword());
            return ResponseEntity.ok(appUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
