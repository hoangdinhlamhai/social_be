package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.domain.City;
import com.hdlh.social_be.domain.Country;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.ProfileRequestDTO;
import com.hdlh.social_be.domain.dto.ProfileResponseDTO;
import com.hdlh.social_be.repository.AccountRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CityService cityService;

    public UserService(UserRepository userRepository, AccountRepository accountRepository, CityService cityService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cityService = cityService;
    }

//    public Account handleGetUserByUsername(String email) {
//        return this.userRepository.findByEmail(email);
//    }

    public ProfileResponseDTO handleGetUserByEmail(String email) {
        User user = userRepository.findByAccount_Email(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        ProfileResponseDTO dto = new ProfileResponseDTO();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCoverPhotoUrl(user.getCoverPhotoUrl());
        dto.setBio(user.getBio());

        if (user.getCity() != null) {
            City city = user.getCity();
            dto.setCityId(city.getId());
            dto.setCityName(city.getName());

            if (city.getCountry() != null) {
                Country country = city.getCountry();
                dto.setCountryId(country.getId());
                dto.setCountryName(country.getName());
            }
        }

        return dto;
    }


//    public ProfileResponseDTO handleGetUserById(long id) {
//        User user = userRepository.findById(id);
//        if (user == null) {
//            throw new RuntimeException("User not found with id: " + id);
//        }
//
//        ProfileResponseDTO dto = new ProfileResponseDTO();
//        dto.setFirstName(user.getFirstName());
//        dto.setLastName(user.getLastName());
//        dto.setAvatarUrl(user.getAvatarUrl());
//        dto.setCoverPhotoUrl(user.getCoverPhotoUrl());
//        dto.setBio(user.getBio());
//
//        if (user.getCity() != null) {
//            City city = user.getCity();
//            dto.setCityId(city.getId());
//            dto.setCityName(city.getName());
//
//            if (city.getCountry() != null) {
//                Country country = city.getCountry();
//                dto.setCountryId(country.getId());
//                dto.setCountryName(country.getName());
//            }
//        }
//
//        return dto;
//    }

    public User handleUpdateInfoUser(ProfileRequestDTO userDTO, long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userDTO.getFirstName() != null) {
            user.setFirstName(userDTO.getFirstName());
        }

        if (userDTO.getLastName() != null) {
            user.setLastName(userDTO.getLastName());
        }

        if (userDTO.getAvatarUrl() != null) {
            user.setAvatarUrl(userDTO.getAvatarUrl());
        }

        if (userDTO.getCoverPhotoUrl() != null) {
            user.setCoverPhotoUrl(userDTO.getCoverPhotoUrl());
        }

        if (userDTO.getBio() != null) {
            user.setBio(userDTO.getBio());
        }

        if (userDTO.getCityId() != 0) {
            City city = cityService.getCityById(userDTO.getCityId())
                    .orElseThrow(() -> new RuntimeException("City not found"));
            user.setCity(city);
        }

        return userRepository.save(user);
    }

}
