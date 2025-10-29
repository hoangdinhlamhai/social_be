package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.FriendRequest;
import com.hdlh.social_be.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findAllByReceiverIdAndStatus(Long receiverId, Status status);

    List<FriendRequest> findAllBySenderIdAndStatus(Long senderId, Status status);

    void deleteById(Long id);
}
