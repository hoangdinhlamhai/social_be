package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    public List<Message> findAllByRoomId(Long roomId);
}
