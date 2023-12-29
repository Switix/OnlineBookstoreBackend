package com.switix.onlinebookstore.controller;

import com.switix.onlinebookstore.model.City;
import com.switix.onlinebookstore.repository.CityRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cities")
public class CityController {

    private final CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping
    List<City> getAllCities(){
        return cityRepository.findAll();
    }
}
