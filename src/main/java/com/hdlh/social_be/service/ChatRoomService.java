package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.ChatRoom;
import com.hdlh.social_be.domain.ChatRoomParticipant;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.chat_realtime.ChatRoomResponseDTO;
import com.hdlh.social_be.repository.ChatRoomRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {
    private ChatRoomRepository chatRoomRepository;
    private UserRepository userRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    // Đặt @Transactional để đảm bảo tất cả các thao tác (tạo room, tạo participant)
    // hoặc thành công cùng lúc, hoặc thất bại cùng lúc.
    @Transactional
    public ChatRoomResponseDTO handleCreateChat(Long userLoginId, Long userGuestId) {
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

        this.chatRoomRepository.save(savedChatRoom);

        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO();
        responseDTO.setId(savedChatRoom.getId());
        responseDTO.setCreatedAt(savedChatRoom.getCreatedAt());

        Set<Long> userIds = savedChatRoom.getParticipants().stream()
                .map(participant -> participant.getUser().getId())
                .collect(Collectors.toSet());
        responseDTO.setParticipantUserIds(userIds);

        return responseDTO;
    }
}
