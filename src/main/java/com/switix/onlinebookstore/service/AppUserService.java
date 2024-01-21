package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AppUserChangePasswordDto;
import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.UpdateAppUserProfileDto;
import com.switix.onlinebookstore.model.AppUser;

public interface AppUserService {
    AppUserDto updateUser(AppUser authenticatedUser, UpdateAppUserProfileDto updateAppUserProfileDto);

    void changeAppUserPassword(AppUser authenticatedUser, AppUserChangePasswordDto appUserChangePasswordDto);

}
