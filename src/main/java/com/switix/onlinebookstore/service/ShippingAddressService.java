package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.ShippingAddressDto;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.ShippingAddress;

public interface ShippingAddressService {
    ShippingAddress getShippingAddress(Long shippingAddressId);
    ShippingAddress createShippingAddress(AppUser authenticatedUser, ShippingAddressDto shippingAddressDto);
    ShippingAddress updateShippingAddress(AppUser authenticatedUser,ShippingAddressDto shippingAddressDto);
    void unbindShippingAddress(AppUser authenticatedUser,Long shippingAddressId);
}
