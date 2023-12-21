package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.RegisterDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    AppUserDto register(RegisterDto registerDto);

    AppUserDto authenticateUser(String email, String password, HttpServletRequest request, HttpServletResponse response) throws Exception;

    void logoutUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication);
}
