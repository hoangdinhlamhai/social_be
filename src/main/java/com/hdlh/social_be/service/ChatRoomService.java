package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.ChatRoom;
import com.hdlh.social_be.domain.ChatRoomParticipant;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.repository.ChatRoomRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ChatRoomService {
    private ChatRoomRepository chatRoomRepository;
    private UserRepository userRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    public ChatRoom handleCreateChat(Long userLoginId, Long userGuestId) {
        User userLogin = userRepository.findById(userLoginId).get();
        User userGuest = userRepository.findById(userGuestId).get();
        ChatRoom chatRoom = new ChatRoom();

        //save room first
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // Tạo hai thực thể "người tham gia" (Participant)
        ChatRoomParticipant participant1 = new ChatRoomParticipant(savedChatRoom, userLogin);
        ChatRoomParticipant participant2 = new ChatRoomParticipant(savedChatRoom, userGuest);

        // Lấy ra cái Set đang tồn tại (đang rỗng)
        Set<ChatRoomParticipant> participantsSet = savedChatRoom.getParticipants();
        participantsSet.add(participant1);
        participantsSet.add(participant2);

        return chatRoomRepository.save(chatRoom);
    }
}
