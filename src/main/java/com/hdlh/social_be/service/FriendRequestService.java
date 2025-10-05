package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.FriendRequest;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.enums.Status;
import com.hdlh.social_be.repository.FriendRequestRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendRequestService {
    private FriendRequestRepository friendRequestRepository;
    private UserRepository userRepository;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    public FriendRequest createFriendRequest(Long senderId, Long receiverId) {
        FriendRequest newFriendRequest = new FriendRequest();
        newFriendRequest.setStatus(Status.PENDING);

        Optional<User> senderOptional = userRepository.findById(senderId);
        if (senderOptional.isPresent()) {
            newFriendRequest.setSender(senderOptional.get());
        }

        Optional<User> receiverOptional = userRepository.findById(receiverId);
        if (receiverOptional.isPresent()) {
            newFriendRequest.setReceiver(receiverOptional.get());
        }

        return friendRequestRepository.save(newFriendRequest);
    }

    public List<FriendRequest> handleGetFriendRequestsByReceiverId(Long receiverId) {
        return this.friendRequestRepository.findAllByReceiverIdAndStatus(receiverId, Status.PENDING);
    }

    public List<FriendRequest> handleGetFriendRequestsBySenderId(Long senderId) {
        return this.friendRequestRepository.findAllBySenderIdAndStatus(senderId, Status.PENDING);
    }

    public FriendRequest handleAcceptFriendRequest(Long id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElse(null);
        friendRequest.setStatus(Status.ACCEPTED);
        this.friendRequestRepository.save(friendRequest);
        return friendRequest;
    }
}
