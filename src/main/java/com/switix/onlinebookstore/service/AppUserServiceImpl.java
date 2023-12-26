package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AppUserChangePasswordDto;
import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.UpdateAppUserProfileDto;
import com.switix.onlinebookstore.exception.InvalidPasswordException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUserDto updateUser(AppUser authenticatedUser, UpdateAppUserProfileDto updateAppUserProfileDto) {

        if (!passwordEncoder.matches(updateAppUserProfileDto.getPassword(), authenticatedUser.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }

        if (!updateAppUserProfileDto.getName().isEmpty()) {
            authenticatedUser.setName(updateAppUserProfileDto.getName());
        }

        if (!updateAppUserProfileDto.getLastname().isEmpty()) {
            authenticatedUser.setLastname(updateAppUserProfileDto.getLastname());
        }

        if (!updateAppUserProfileDto.getEmail().isEmpty()) {
            authenticatedUser.setEmail(updateAppUserProfileDto.getEmail());
        }
        AppUser updatedUser = userRepository.save(authenticatedUser);

        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setEmail(updatedUser.getEmail());
        appUserDto.setName(updatedUser.getName());
        appUserDto.setLastname(updatedUser.getLastname());
        appUserDto.setRole(updatedUser.getRole().getName());
        appUserDto.setId(updatedUser.getId());

        return appUserDto;
    }

    @Override
    public void changeAppUserPassword(AppUser authenticatedUser, AppUserChangePasswordDto appUserChangePasswordDto) {
        if (!passwordEncoder.matches(appUserChangePasswordDto.getPassword(), authenticatedUser.getPassword())) {
            throw new InvalidPasswordException("Incorrect password");
        }
        String newPasswordEncoded = passwordEncoder.encode(appUserChangePasswordDto.getNewPassword());
        authenticatedUser.setPassword(newPasswordEncoded);

        userRepository.save(authenticatedUser);

    }
}
