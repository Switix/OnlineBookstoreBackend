package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.ShippingAddressDto;
import com.switix.onlinebookstore.exception.CityNotFoundException;
import com.switix.onlinebookstore.exception.CountryNotFoundException;
import com.switix.onlinebookstore.exception.ShippingAddressNotFoundException;
import com.switix.onlinebookstore.exception.UnauthorizedAccessException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.City;
import com.switix.onlinebookstore.model.Country;
import com.switix.onlinebookstore.model.ShippingAddress;
import com.switix.onlinebookstore.repository.CityRepository;
import com.switix.onlinebookstore.repository.CountryRepository;
import com.switix.onlinebookstore.repository.ShippingAddressRepository;
import org.springframework.stereotype.Service;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public ShippingAddressServiceImpl(ShippingAddressRepository shippingAddressRepository, CountryRepository countryRepository, CityRepository cityRepository) {
        this.shippingAddressRepository = shippingAddressRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public ShippingAddress getShippingAddress(Long shippingAddressId) {
        return shippingAddressRepository.findById(shippingAddressId)
                .orElseThrow(() -> new ShippingAddressNotFoundException("Shipping address not found"));
    }

    @Override
    public ShippingAddress createShippingAddress(AppUser authenticatedUser, ShippingAddressDto shippingAddressDto) {
        ShippingAddress shippingAddress = new ShippingAddress();
        mapToShippingAddress(shippingAddress, shippingAddressDto, authenticatedUser);

        return shippingAddressRepository.save(shippingAddress);
    }

    @Override
    public ShippingAddress updateShippingAddress(AppUser authenticatedUser, ShippingAddressDto shippingAddressDto) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingAddressDto.getId())
                .orElseThrow(() -> new ShippingAddressNotFoundException("Shipping address not found"));

        // Check if the retrieved shipping address belongs to the authenticated user
        if (shippingAddress.getAppUser() == null || !shippingAddress.getAppUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to the Shipping address");
        }

        mapToShippingAddress(shippingAddress, shippingAddressDto, authenticatedUser);

        return shippingAddressRepository.save(shippingAddress);
    }

    @Override
    public void unbindShippingAddress(AppUser authenticatedUser, Long shippingAddressId) {
        ShippingAddress shippingAddress = shippingAddressRepository.findById(shippingAddressId)
                .orElseThrow(() -> new ShippingAddressNotFoundException("Shipping address not found"));

        // Check if the retrieved shipping address belongs to the authenticated user
        if (shippingAddress.getAppUser() == null || !shippingAddress.getAppUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to the Shipping address");
        }

        // Unbind the shipping address from the user
        shippingAddress.setAppUser(null);
        shippingAddressRepository.save(shippingAddress);
    }

    private void mapToShippingAddress(ShippingAddress shippingAddress, ShippingAddressDto shippingAddressDto, AppUser authenticatedUser) {

        Country country = countryRepository.findById(shippingAddressDto.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));

        City city = cityRepository.findById(shippingAddressDto.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        shippingAddress.setApartmentNumber(shippingAddressDto.getApartmentNumber());
        shippingAddress.setBuildingNumber(shippingAddressDto.getBuildingNumber());
        shippingAddress.setZipCode(shippingAddressDto.getZipCode());
        shippingAddress.setStreet(shippingAddressDto.getStreet());
        shippingAddress.setCountry(country);
        shippingAddress.setCity(city);
        shippingAddress.setAppUser(authenticatedUser);
        shippingAddress.setName(shippingAddressDto.getName());
    }
}
