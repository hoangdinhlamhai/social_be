package com.hdlh.social_be.domain.dto.chat_realtime;

public record ChatMessageDTO(
        Long roomId,
        Long senderId,
        String senderName, // Gửi kèm tên để hiển thị
        String content,
        String sentAt
) {
}
