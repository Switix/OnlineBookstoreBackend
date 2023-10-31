package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.RegisterDto;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.Role;
import com.switix.onlinebookstore.repository.AppUserRepository;
import com.switix.onlinebookstore.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public AppUser register(RegisterDto registerDto) {
        if(appUserRepository.existsByUsername(registerDto.getUsername())){
            return null;
        }
        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setPassword(passwordEncoder.encode((registerDto.getPassword())));
        appUser.setEmail(registerDto.getEmail());

        Role role = roleRepository.findByName("ROLE_CUSTOMER").get();
        appUser.setRole(role);

        return appUserRepository.save(appUser);
    }
}
