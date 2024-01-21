package com.switix.onlinebookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailCreationDto {
    @JsonProperty("billingAddress")
    private BillingAddressDto billingAddressDto;
    @JsonProperty("shippingAddress")
    private ShippingAddressDto shippingAddressDto;
}
