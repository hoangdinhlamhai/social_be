package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.domain.User;
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

    public User handleAddInfoUser(User user, long userId) {
        Optional<Account> acc = this.accountRepository.findById(userId);
        user.setAccount(acc.get());
        return userRepository.save(user);
    }

    public User handleUpdateInfoUser(User user) {
        return userRepository.save(user);
    }
}
