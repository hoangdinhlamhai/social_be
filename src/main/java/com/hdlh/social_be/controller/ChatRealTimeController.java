package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.chat_realtime.ChatMessageDTO;
import com.hdlh.social_be.service.ChatRealTimeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

//khác với REST Controller, dùng @MessageMapping để xử lý các tin nhắn gửi qua WebSocket.
@Controller
public class ChatRealTimeController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRealTimeService chatRealTimeService;

    public ChatRealTimeController(SimpMessagingTemplate messagingTemplate, ChatRealTimeService chatRealTimeService) {
        this.messagingTemplate = messagingTemplate;
        this.chatRealTimeService = chatRealTimeService;
    }

    // Xử lý tin nhắn gửi đến "/app/chat.sendMessage"
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage) {
        // Lưu tin nhắn vào database
        ChatMessageDTO savedMessage = chatRealTimeService.saveMessage(chatMessage);

        // Gửi tin nhắn đến tất cả những ai đang lắng nghe topic của phòng chat đó
        // Ví dụ: /topic/chat/room/123
        messagingTemplate.convertAndSend("/topic/chat/room/" + savedMessage.getRoomId(), savedMessage);
    }
}
