package com.hdlh.social_be.domain.dto.chat_realtime;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long roomId;
    private Long senderId;
    private String senderName; // Gửi kèm tên để hiển thị
    private String content;
    private String sentAt;
}
