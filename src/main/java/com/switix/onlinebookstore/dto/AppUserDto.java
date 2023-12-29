package com.switix.onlinebookstore.dto;

import com.switix.onlinebookstore.model.BillingAddress;
import com.switix.onlinebookstore.model.ShippingAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<ShippingAddress> shippingAddresses;
}
