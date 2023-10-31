package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.dto.RegisterDto;
import com.switix.onlinebookstore.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8081")
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
}
