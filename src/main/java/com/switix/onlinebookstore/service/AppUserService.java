package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AppUserChangePasswordDto;
import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.ChangeBillingAddressDto;
import com.switix.onlinebookstore.dto.UpdateAppUserProfileDto;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.BillingAddress;

public interface AppUserService {
    AppUserDto updateUser(AppUser authenticatedUser, UpdateAppUserProfileDto updateAppUserProfileDto);

    void changeAppUserPassword(AppUser authenticatedUser, AppUserChangePasswordDto appUserChangePasswordDto);

    BillingAddress changeBillingAddress(AppUser authenticatedUser, ChangeBillingAddressDto changeBillingAddressDto);
}
