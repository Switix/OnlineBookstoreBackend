package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.*;
import com.switix.onlinebookstore.exception.CityNotFoundException;
import com.switix.onlinebookstore.exception.CountryNotFoundException;
import com.switix.onlinebookstore.exception.InvalidPasswordException;
import com.switix.onlinebookstore.model.*;
import com.switix.onlinebookstore.repository.AppUserRepository;
import com.switix.onlinebookstore.repository.CityRepository;
import com.switix.onlinebookstore.repository.CountryRepository;
import com.switix.onlinebookstore.repository.ShippingAddressRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final ShippingAddressRepository shippingAddressRepository;

    public AppUserServiceImpl(AppUserRepository userRepository, PasswordEncoder passwordEncoder, CityRepository cityRepository, CountryRepository countryRepository, ShippingAddressRepository shippingAddressRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.shippingAddressRepository = shippingAddressRepository;
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
        appUserDto.setBillingAddress(updatedUser.getBillingAddress());

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

    @Override
    public BillingAddress changeBillingAddress(AppUser authenticatedUser, ChangeBillingAddressDto changeBillingAddressDto) {
        Country country = countryRepository.findById(changeBillingAddressDto.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));

        City city = cityRepository.findById(changeBillingAddressDto.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));


        BillingAddress billingAddress = authenticatedUser.getBillingAddress();
        if (billingAddress == null) {
            billingAddress = new BillingAddress();
            authenticatedUser.setBillingAddress(billingAddress);
        }

        billingAddress.setApartmentNumber(changeBillingAddressDto.getApartmentNumber());
        billingAddress.setBuildingNumber(changeBillingAddressDto.getBuildingNumber());
        billingAddress.setZipCode(changeBillingAddressDto.getZipCode());
        billingAddress.setStreet(changeBillingAddressDto.getStreet());
        billingAddress.setCountry(country);
        billingAddress.setCity(city);
        billingAddress.setPhoneNumber(changeBillingAddressDto.getPhoneNumber());


        AppUser updatedUser = userRepository.save(authenticatedUser);

        return updatedUser.getBillingAddress();
    }

    @Override
    public ShippingAddress changeShippingAddress(AppUser authenticatedUser, ChangeShippingAddressDto changeShippingAddressDto) {

        Country country = countryRepository.findById(changeShippingAddressDto.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));

        City city = cityRepository.findById(changeShippingAddressDto.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        ShippingAddress shippingAddress = shippingAddressRepository.findById(changeShippingAddressDto.getId())
                .orElseGet(ShippingAddress::new);

        shippingAddress.setAppUser(authenticatedUser);

        shippingAddress.setApartmentNumber(changeShippingAddressDto.getApartmentNumber());
        shippingAddress.setBuildingNumber(changeShippingAddressDto.getBuildingNumber());
        shippingAddress.setZipCode(changeShippingAddressDto.getZipCode());
        shippingAddress.setStreet(changeShippingAddressDto.getStreet());
        shippingAddress.setCountry(country);
        shippingAddress.setCity(city);
        shippingAddress.setName(changeShippingAddressDto.getName());


        return shippingAddressRepository.save(shippingAddress);

    }

    @Override
    public ShippingAddress createShippingAddress(AppUser authenticatedUser, ChangeShippingAddressDto changeShippingAddressDto) {
        Country country = countryRepository.findById(changeShippingAddressDto.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));

        City city = cityRepository.findById(changeShippingAddressDto.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        ShippingAddress shippingAddress = new ShippingAddress();

        shippingAddress.setAppUser(authenticatedUser);

        shippingAddress.setApartmentNumber(changeShippingAddressDto.getApartmentNumber());
        shippingAddress.setBuildingNumber(changeShippingAddressDto.getBuildingNumber());
        shippingAddress.setZipCode(changeShippingAddressDto.getZipCode());
        shippingAddress.setStreet(changeShippingAddressDto.getStreet());
        shippingAddress.setCountry(country);
        shippingAddress.setCity(city);
        shippingAddress.setName(changeShippingAddressDto.getName());


        return shippingAddressRepository.save(shippingAddress);

    }
}
