package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.RegisterDto;
import com.switix.onlinebookstore.model.AppUser;

public interface AuthService {
    AppUser register(RegisterDto registerDto);
}
