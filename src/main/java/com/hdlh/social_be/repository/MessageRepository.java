package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
