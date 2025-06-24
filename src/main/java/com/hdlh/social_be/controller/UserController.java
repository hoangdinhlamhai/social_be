package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users/{userId}/profile")
    public ResponseEntity<User> addInfoUser(@RequestBody User user, @PathVariable Long userId) {
        return ResponseEntity.ok(this.userService.handleAddInfoUser(user, userId));
    }

    @PutMapping("/api/users/{userId}/profile")
    public ResponseEntity<User> updateInfoUser(@RequestBody User user) {
        return ResponseEntity.ok(this.userService.handleUpdateInfoUser(user));
    }
}
