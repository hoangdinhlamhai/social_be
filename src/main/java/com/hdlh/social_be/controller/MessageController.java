package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.Message;
import com.hdlh.social_be.domain.dto.chat_realtime.ChatMessageDTO;
import com.hdlh.social_be.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/get-msg-by-roomid/{roomId}")
    public ResponseEntity<List<ChatMessageDTO>> getAllMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(this.messageService.handleGetAllMessagesByRoomId(roomId));
    }
}
