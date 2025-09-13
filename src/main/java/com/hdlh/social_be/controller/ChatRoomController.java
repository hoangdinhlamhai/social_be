package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.chat_realtime.ChatRoomResponseDTO;
import com.hdlh.social_be.domain.dto.chat_realtime.UserInRoomChatDTO;
import com.hdlh.social_be.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {
    private ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping("/create-room")
    public ResponseEntity<ChatRoomResponseDTO> createChatRoom(@RequestBody UserInRoomChatDTO userInRoomChatDTO) {
        return ResponseEntity.ok(this.chatRoomService.handleCreateChat(userInRoomChatDTO.getUserIdLogin(), userInRoomChatDTO.getUserId()));
    }

    @GetMapping("/get-chat-room")
    public ResponseEntity<ChatRoomResponseDTO> getChatRoomById(@RequestParam Long roomId) {
        return ResponseEntity.ok(this.chatRoomService.handleGetChatRoom(roomId));
    }

    @GetMapping("/get-chat-room-by-participantsId")
    public ResponseEntity<ChatRoomResponseDTO> getChatRoomByUserId(@RequestParam Long userLoginId, @RequestParam Long userGuestId) {
        return ResponseEntity.ok(this.chatRoomService.handleGetChatRoomByParticipantId(userLoginId, userGuestId));
    }

    @GetMapping("/get-list-chat-room")
    public ResponseEntity<List<ChatRoomResponseDTO>> getAllChatRoomsByUserId(@RequestParam Long userLoginId) {
        return ResponseEntity.ok(this.chatRoomService.handleGetAllChatRooms(userLoginId));
    }
}
