package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // method find mình tự viết ra sẽ mặc định trả về optional
}
