package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.ProfileRequestDTO;
import com.hdlh.social_be.domain.dto.ProfileResponseDTO;
import com.hdlh.social_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //profile cá nhân
    @GetMapping("/api/me")
    public ResponseEntity<ProfileResponseDTO> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // hoặc getPrincipal()

        ProfileResponseDTO profile = userService.handleGetUserByEmail(email);
        System.out.println("===> FETCHING user:");  // KHÔNG log cả user!
        return ResponseEntity.ok(profile);
    }
    
    @GetMapping("/api/users/{email}")
    public ResponseEntity<ProfileResponseDTO> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.handleGetUserByEmail(email));
    }

    //patch chỉ update trường gửi lên, những trường không gửi sẽ không bị mất như put (phải có điều kiện)
    @PatchMapping("/api/users/{userId}/profile")
    public ResponseEntity<User> addInfoUser(@RequestBody ProfileRequestDTO userDTO, @PathVariable Long userId) {
        return ResponseEntity.ok(this.userService.handleUpdateInfoUser(userDTO, userId));
    }

}
