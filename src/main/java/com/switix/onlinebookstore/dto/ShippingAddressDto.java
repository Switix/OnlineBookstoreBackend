package com.switix.onlinebookstore.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressDto {
    private Long id;
    private String name;
    private Long cityId;
    private String street;
    private String apartmentNumber;
    private String buildingNumber;
    private String zipCode;
    private Long countryId;
}
