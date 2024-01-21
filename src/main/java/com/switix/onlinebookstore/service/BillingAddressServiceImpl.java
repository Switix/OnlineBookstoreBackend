package com.switix.onlinebookstore.service;

import com.switix.onlinebookstore.dto.BillingAddressDto;
import com.switix.onlinebookstore.exception.BillingAddressNotFoundException;
import com.switix.onlinebookstore.exception.CityNotFoundException;
import com.switix.onlinebookstore.exception.CountryNotFoundException;
import com.switix.onlinebookstore.exception.UnauthorizedAccessException;
import com.switix.onlinebookstore.model.AppUser;
import com.switix.onlinebookstore.model.BillingAddress;
import com.switix.onlinebookstore.model.City;
import com.switix.onlinebookstore.model.Country;
import com.switix.onlinebookstore.repository.BillingAddressRepository;
import com.switix.onlinebookstore.repository.CityRepository;
import com.switix.onlinebookstore.repository.CountryRepository;
import org.springframework.stereotype.Service;

@Service
public class BillingAddressServiceImpl implements BillingAddressService {
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final BillingAddressRepository billingAddressRepository;

    public BillingAddressServiceImpl(CountryRepository countryRepository, CityRepository cityRepository, BillingAddressRepository billingAddressRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.billingAddressRepository = billingAddressRepository;
    }

    @Override
    public BillingAddress getBillingAddress(Long billingAddressId) {
        return billingAddressRepository.findById(billingAddressId)
                .orElseThrow(() -> new BillingAddressNotFoundException("Billing address not found"));
    }

    @Override
    public BillingAddress createBillingAddress(AppUser authenticatedUser, BillingAddressDto billingAddressDto) {
        BillingAddress billingAddress = new BillingAddress();
        mapToBillingAddress(billingAddress, billingAddressDto, authenticatedUser);

        return billingAddressRepository.save(billingAddress);
    }

    @Override
    public BillingAddress updateBillingAddress(AppUser authenticatedUser, BillingAddressDto billingAddressDto) {
        BillingAddress billingAddress = billingAddressRepository.findById(billingAddressDto.getId())
                .orElseThrow(() -> new BillingAddressNotFoundException("Billing address not found"));

        // Check if the retrieved billing address belongs to the authenticated user
        if (billingAddress.getAppUser() == null || !billingAddress.getAppUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to the billing address");
        }

        mapToBillingAddress(billingAddress, billingAddressDto, authenticatedUser);
        return billingAddressRepository.save(billingAddress);
    }

    @Override
    //it doesn't delete the billing address io only unbinds it from user to prevent order table break
    public void unbindBillingAddress(AppUser authenticatedUser, Long billingAddressId) {
        BillingAddress billingAddress = billingAddressRepository.findById(billingAddressId)
                .orElseThrow(() -> new BillingAddressNotFoundException("Billing address not found"));

        // Check if the retrieved billing address belongs to the authenticated user
        if (billingAddress.getAppUser() == null || !billingAddress.getAppUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedAccessException("Unauthorized access to the billing address");
        }

        // Unbind the billing address from the user
        billingAddress.setAppUser(null);
        billingAddressRepository.save(billingAddress);
    }

    private void mapToBillingAddress(BillingAddress billingAddress, BillingAddressDto billingAddressDto, AppUser authenticatedUser) {

        Country country = countryRepository.findById(billingAddressDto.getCountryId())
                .orElseThrow(() -> new CountryNotFoundException("Country not found"));

        City city = cityRepository.findById(billingAddressDto.getCityId())
                .orElseThrow(() -> new CityNotFoundException("City not found"));

        billingAddress.setApartmentNumber(billingAddressDto.getApartmentNumber());
        billingAddress.setBuildingNumber(billingAddressDto.getBuildingNumber());
        billingAddress.setZipCode(billingAddressDto.getZipCode());
        billingAddress.setStreet(billingAddressDto.getStreet());
        billingAddress.setCountry(country);
        billingAddress.setCity(city);
        billingAddress.setPhoneNumber(billingAddressDto.getPhoneNumber());
        billingAddress.setAppUser(authenticatedUser);
    }
}
