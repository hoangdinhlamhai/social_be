package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.ChatRoom;
import com.hdlh.social_be.domain.Message;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.chat_realtime.ChatMessageDTO;
import com.hdlh.social_be.repository.ChatRoomRepository;
import com.hdlh.social_be.repository.MessageRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatRealTimeService {
    private UserRepository userRepository;
    private ChatRoomRepository chatRoomRepository;
    private MessageRepository messageRepository;

    public ChatRealTimeService(UserRepository userRepository, ChatRoomRepository chatRoomRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messageRepository = messageRepository;
    }

    public ChatMessageDTO saveMessage(ChatMessageDTO messageDTO){
        User sender = userRepository.findById(messageDTO.getSenderId()).orElse(null);
        //Dùng orElseThrow (nếu muốn bắt buộc tồn tại)
        ChatRoom chatRoom = chatRoomRepository.findById(messageDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("ChatRoom not found"));

        Message messageEntity = new Message();
        messageEntity.setRoom(chatRoom);
        messageEntity.setSender(sender);
        messageEntity.setContent(messageDTO.getContent());
        messageEntity.setSentAt(LocalDateTime.now());

        messageRepository.save(messageEntity);

        // map lại thông tin trước khi trả về (ví dụ thêm tên người gửi)
        messageDTO.setSenderName(sender.getFirstName() + " " + sender.getLastName());
        messageDTO.setSentAt(messageEntity.getSentAt());

        return messageDTO;
    }
}
