package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
