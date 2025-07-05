package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    public List<City> findByCountryId(Long countryId);

}
