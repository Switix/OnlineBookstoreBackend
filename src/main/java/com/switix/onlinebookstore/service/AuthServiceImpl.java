package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.AppUserDto;
import com.switix.onlinebookstore.dto.RegisterDto;
import com.switix.onlinebookstore.exception.EmailAlreadyExistsException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.Role;
import com.switix.onlinebookstore.repository.AppUserRepository;
import com.switix.onlinebookstore.repository.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    public AuthServiceImpl(PasswordEncoder passwordEncoder, AppUserRepository appUserRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AppUserDto register(RegisterDto registerDto) {
        String email = registerDto.getEmail();
        appUserRepository.findByEmail(email).ifPresent((user) -> {
            throw new EmailAlreadyExistsException("Email already exists: " + email);
        });

        AppUser appUser = new AppUser();
        appUser.setName(registerDto.getName());
        appUser.setLastname(registerDto.getLastname());
        appUser.setPassword(passwordEncoder.encode((registerDto.getPassword())));
        appUser.setEmail(registerDto.getEmail());

        Role role = roleRepository.findByName("ROLE_CUSTOMER").get();
        appUser.setRole(role);
        AppUser savedUser = appUserRepository.save(appUser);

        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setEmail(savedUser.getEmail());
        appUserDto.setName(savedUser.getName());
        appUserDto.setLastname(savedUser.getLastname());
        appUserDto.setRole(savedUser.getRole().getName());
        appUserDto.setId(savedUser.getId());
        appUserDto.setBillingAddress(savedUser.getBillingAddress());
        return appUserDto;
    }

    @Override
    public AppUserDto authenticateUser(String email, String password , HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);


            AppUser appUser = (AppUser) authentication.getPrincipal();
            return new AppUserDto(appUser.getId(), appUser.getName(),appUser.getLastname(), appUser.getEmail(), appUser.getRole().getName(),appUser.getBillingAddress());

        } catch (BadCredentialsException e) {
            throw new Exception("Bad credentials", e);
        }
    }

    @Override
    public void logoutUser(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response,authentication);
    }
}
