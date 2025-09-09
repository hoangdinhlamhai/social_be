package com.hdlh.social_be.domain.dto.chat_realtime;

import com.hdlh.social_be.domain.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ChatRoomResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Set<User> participantUser;
}
