package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.BillingAddressDto;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.BillingAddress;

public interface BillingAddressService {
    BillingAddress getBillingAddress(Long billingAddressId);
    BillingAddress createBillingAddress(AppUser authenticatedUser,BillingAddressDto billingAddressDto);
    BillingAddress updateBillingAddress(AppUser authenticatedUser, BillingAddressDto billingAddressDto);
    void unbindBillingAddress(AppUser authenticatedUser,Long billingAddressId);
}
