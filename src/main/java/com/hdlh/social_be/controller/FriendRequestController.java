package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.FriendRequest;
import com.hdlh.social_be.domain.dto.friend_request.FriendRequestDTO;
import com.hdlh.social_be.domain.dto.friend_request.FriendRequestResponseDTO;
import com.hdlh.social_be.domain.enums.Status;
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

    @GetMapping("/receiver/{status}")
    public ResponseEntity<List<FriendRequestResponseDTO>> getFriendRequestsByReceiverId(@RequestParam("receiverId") Long receiverId, @PathVariable("status") Status status) {
        return ResponseEntity.ok(this.friendRequestService.handleGetFriendRequestsByReceiverId(receiverId, status));
    }

    @GetMapping("/sender/{status}")
    public ResponseEntity<List<FriendRequestResponseDTO>> getFriendRequestsBySenderId(@RequestParam("senderId") Long senderId, @PathVariable("status") Status status) {
        return ResponseEntity.ok(this.friendRequestService.handleGetFriendRequestsBySenderId(senderId, status));
    }

    @PostMapping("/accept/{friendRequestId}")
    public ResponseEntity<FriendRequest> acceptFriendRequest(@PathVariable("friendRequestId") Long friendRequestId) {
        return ResponseEntity.ok(this.friendRequestService.handleAcceptFriendRequest(friendRequestId));
    }

    @PostMapping("/decline/{friendRequestId}")
    public ResponseEntity<FriendRequest> declineFriendRequest(@PathVariable("friendRequestId") Long friendRequestId) {
        return ResponseEntity.ok(this.friendRequestService.handleDeclineFriendRequest(friendRequestId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<FriendRequest> deleteFriend(@PathVariable Long id) {
        return ResponseEntity.ok(this.friendRequestService.handleDeleteFriendById(id));
    }
}
