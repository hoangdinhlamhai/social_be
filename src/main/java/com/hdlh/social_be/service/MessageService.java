package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Message;
import com.hdlh.social_be.domain.dto.chat_realtime.ChatMessageDTO;
import com.hdlh.social_be.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<ChatMessageDTO> handleGetAllMessagesByRoomId(Long roomId) {
        List<Message> messages = messageRepository.findAllByRoomId(roomId);
        List<ChatMessageDTO> chatMessageDTOs = new ArrayList<>();
        if(!messages.isEmpty()) {
            for(Message message : messages) {
                ChatMessageDTO chatMessageDTO = new ChatMessageDTO();

                chatMessageDTO.setRoomId(message.getRoom().getId());
                chatMessageDTO.setSenderId(message.getSender().getId());
                chatMessageDTO.setSenderName(message.getSender().getFirstName() + " " + message.getSender().getLastName());
                chatMessageDTO.setContent(message.getContent());
                chatMessageDTO.setSentAt(message.getSentAt());

                chatMessageDTOs.add(chatMessageDTO);
            }
        }

        return chatMessageDTOs;
    }
}
