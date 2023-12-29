package com.switix.onlinebookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeBillingAddressDto {
    private String phoneNumber;
    private Long cityId;
    private String street;
    private String apartmentNumber;
    private String buildingNumber;
    private String zipCode;
    private Long countryId;
}
