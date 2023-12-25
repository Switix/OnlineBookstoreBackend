package com.switix.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppUserProfileDto {
    private String name;
    private String lastname;
    private String email;
    private String password; // password for verification

}
