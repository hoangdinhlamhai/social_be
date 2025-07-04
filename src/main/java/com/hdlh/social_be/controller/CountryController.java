package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.Country;
import com.hdlh.social_be.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {
    public final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("")
    public ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity.ok(this.countryService.getAllCountries());
    }
}
