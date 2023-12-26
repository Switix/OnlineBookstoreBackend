package com.switix.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AppUserChangePasswordDto {
    private String password; // password for verification
    private String newPassword;
}

