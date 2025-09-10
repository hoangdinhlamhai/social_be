package com.hdlh.social_be.domain.dto.chat_realtime;

import com.hdlh.social_be.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ChatRoomResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Set<ParticipantInfoDTO> participantUser;
}
