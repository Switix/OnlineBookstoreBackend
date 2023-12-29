package com.switix.onlinebookstore.dto;

import com.switix.onlinebookstore.model.BillingAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String role;
    private BillingAddress billingAddress;
}
