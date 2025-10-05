package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.FriendRequest;
import com.hdlh.social_be.domain.dto.friend_request.FriendRequestDTO;
import com.hdlh.social_be.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friend-requests")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @PostMapping()
    public ResponseEntity<FriendRequest> addFriendRequest(@RequestBody FriendRequestDTO friendRequestDTO) {
        return ResponseEntity.ok(this.friendRequestService.createFriendRequest(friendRequestDTO.getSenderId(), friendRequestDTO.getReceiverId()));
    }

    @GetMapping("/receiver")
    public ResponseEntity<List<FriendRequest>> getFriendRequestsByReceiverId(@RequestParam("receiverId") Long receiverId) {
        return ResponseEntity.ok(this.friendRequestService.handleGetFriendRequestsByReceiverId(receiverId));
    }

    @GetMapping("/sender")
    public ResponseEntity<List<FriendRequest>> getFriendRequestsBySenderId(@RequestParam("senderId") Long senderId) {
        return ResponseEntity.ok(this.friendRequestService.handleGetFriendRequestsBySenderId(senderId));
    }

    @PostMapping("/accept/{friendRequestId}")
    public ResponseEntity<FriendRequest> acceptFriendRequest(@PathVariable("friendRequestId") Long friendRequestId) {
        return ResponseEntity.ok(this.friendRequestService.handleAcceptFriendRequest(friendRequestId));
    }
}
