package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.ChatRoom;
import com.hdlh.social_be.domain.dto.chat_realtime.UserInRoomChatDTO;
import com.hdlh.social_be.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {
    private ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/create-room")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody UserInRoomChatDTO userInRoomChatDTO) {
        return ResponseEntity.ok(this.chatRoomService.handleCreateChat(userInRoomChatDTO.getUserIdLogin(), userInRoomChatDTO.getUserId()));
    }
}
