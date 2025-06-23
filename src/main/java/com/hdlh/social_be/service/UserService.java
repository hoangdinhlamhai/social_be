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

//    public Account handleCreateUser(Account user) {
//        return userRepository.save(user);
//    }

    public Account handleGetUserByUsername(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void updateRefreshTokenUser(String refreshToken, String email) {
        Account user = this.userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);
        }
    }

    //api logout
    public void clearRefreshToken(String email) {
        Optional<Account> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        userOptional.ifPresent(user -> {
            user.setRefreshToken(null);
            userRepository.save(user);
        });
    }

    //api refresh token
    public Account getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }
}
