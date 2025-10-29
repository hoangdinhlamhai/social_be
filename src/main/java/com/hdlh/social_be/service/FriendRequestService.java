package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.FriendRequest;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.friend_request.FriendRequestResponseDTO;
import com.hdlh.social_be.domain.enums.Status;
import com.hdlh.social_be.repository.FriendRequestRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<FriendRequestResponseDTO> handleGetFriendRequestsByReceiverId(Long receiverId, Status status) {
        List<FriendRequest> requests = friendRequestRepository.findAllByReceiverIdAndStatus(receiverId, status);

        return requests.stream().map(
            req -> new FriendRequestResponseDTO(
                    req.getId(),
                    req.getStatus(),
                    new FriendRequestResponseDTO.UserInfo(
                            req.getSender().getId(),
                            req.getSender().getAccount().getEmail(),
                            req.getSender().getFirstName(),
                            req.getSender().getLastName(),
                            req.getSender().getAvatarUrl(),
                            req.getSender().getCoverPhotoUrl(),
                            req.getSender().getBio()
                    ),
                    new FriendRequestResponseDTO.UserInfo(
                            req.getReceiver().getId(),
                            req.getReceiver().getAccount().getEmail(),
                            req.getReceiver().getFirstName(),
                            req.getReceiver().getLastName(),
                            req.getReceiver().getAvatarUrl(),
                            req.getReceiver().getCoverPhotoUrl(),
                            req.getReceiver().getBio()
                    )
            )
        ).collect(Collectors.toList());
    }

    public List<FriendRequestResponseDTO> handleGetFriendRequestsBySenderId(Long senderId, Status status) {
        List<FriendRequest> requests = friendRequestRepository.findAllBySenderIdAndStatus(senderId, status);

        return requests.stream().map(
                req -> new FriendRequestResponseDTO(
                        req.getId(),
                        req.getStatus(),
                        new FriendRequestResponseDTO.UserInfo(
                                req.getSender().getId(),
                                req.getSender().getAccount().getEmail(),
                                req.getSender().getFirstName(),
                                req.getSender().getLastName(),
                                req.getSender().getAvatarUrl(),
                                req.getSender().getCoverPhotoUrl(),
                                req.getSender().getBio()
                        ),
                        new FriendRequestResponseDTO.UserInfo(
                                req.getReceiver().getId(),
                                req.getReceiver().getAccount().getEmail(),
                                req.getReceiver().getFirstName(),
                                req.getReceiver().getLastName(),
                                req.getReceiver().getAvatarUrl(),
                                req.getReceiver().getCoverPhotoUrl(),
                                req.getReceiver().getBio()
                        )
                )
        ).collect(Collectors.toList());
    }

    public FriendRequest handleAcceptFriendRequest(Long id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElse(null);
        friendRequest.setStatus(Status.ACCEPTED);
        this.friendRequestRepository.save(friendRequest);
        return friendRequest;
    }

    public FriendRequest handleDeclineFriendRequest(Long id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).orElse(null);
        friendRequest.setStatus(Status.DECLINED);
        this.friendRequestRepository.save(friendRequest);
        return friendRequest;
    }

    public FriendRequest handleDeleteFriendById(Long id) {
        this.friendRequestRepository.deleteById(id);
        return null;
    }
}
