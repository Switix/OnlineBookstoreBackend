package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.*;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.BillingAddress;
import com.switix.onlinebookstore.model.ShippingAddress;

public interface AppUserService {
    AppUserDto updateUser(AppUser authenticatedUser, UpdateAppUserProfileDto updateAppUserProfileDto);

    void changeAppUserPassword(AppUser authenticatedUser, AppUserChangePasswordDto appUserChangePasswordDto);

    BillingAddress changeBillingAddress(AppUser authenticatedUser, ChangeBillingAddressDto changeBillingAddressDto);

    ShippingAddress changeShippingAddress(AppUser authenticatedUser, ChangeShippingAddressDto changeShippingAddressDto);

    ShippingAddress createShippingAddress(AppUser authenticatedUser, ChangeShippingAddressDto changeShippingAddressDto);
}
