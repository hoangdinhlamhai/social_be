package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.City;
import com.hdlh.social_be.service.CityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    public final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/by-country/{countryId}")
    public ResponseEntity<List<City>> getCitiesByCountry(@PathVariable Long countryId) {
        return ResponseEntity.ok(cityService.getCitiesByCountry(countryId));
    }
}
