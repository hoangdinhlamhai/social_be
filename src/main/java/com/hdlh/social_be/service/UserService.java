package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public Account handleGetUserByUsername(String email) {
//        return this.userRepository.findByEmail(email);
//    }
}
