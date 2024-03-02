package com.switix.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailCreationDto {
    private Long BillingAddressId ;
    private Long ShippingAddressId ;
    private Long payMethodId;
    private Long shipmentMethodId;
}
