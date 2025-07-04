package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.City;
import com.hdlh.social_be.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    public final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<City> getCitiesByCountry(Long countryId) {
        return cityRepository.findByCountryId(countryId);
    }
}
