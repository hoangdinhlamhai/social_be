package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.ProfileRequestDTO;
import com.hdlh.social_be.repository.AccountRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public UserService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

//    public Account handleGetUserByUsername(String email) {
//        return this.userRepository.findByEmail(email);
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

        return userRepository.save(user);
    }

}
