package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Country;
import com.hdlh.social_be.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    public final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<Country> getAllCountries() {
        return this.countryRepository.findAll();
    }
}
