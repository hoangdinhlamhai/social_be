package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.FriendRequest;
import com.hdlh.social_be.domain.dto.friend_request.FriendRequestDTO;
import com.hdlh.social_be.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend-request")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping()
    public ResponseEntity<FriendRequest> addFriendRequest(@RequestBody FriendRequestDTO friendRequestDTO) {
        return ResponseEntity.ok(this.friendRequestService.createFriendRequest(friendRequestDTO.getSenderId(), friendRequestDTO.getReceiverId()));
    }
}
