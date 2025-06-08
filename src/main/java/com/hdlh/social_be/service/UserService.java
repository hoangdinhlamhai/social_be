package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleCreateUser(User user) {
        return userRepository.save(user);
    }
}
