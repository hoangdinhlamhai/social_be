package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.ChatRoom;
import com.hdlh.social_be.domain.ChatRoomParticipant;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.chat_realtime.ChatRoomResponseDTO;
import com.hdlh.social_be.domain.dto.chat_realtime.ParticipantInfoDTO;
import com.hdlh.social_be.repository.ChatRoomRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

        Set<ParticipantInfoDTO> participantDTOs = savedChatRoom.getParticipants().stream()
                .map(participant -> {
                    ParticipantInfoDTO participantDTO = new ParticipantInfoDTO();
                    User user = participant.getUser();
                    participantDTO.setId(user.getId());
                    participantDTO.setFirstName(user.getFirstName());
                    participantDTO.setLastName(user.getLastName());
                    participantDTO.setAvatar(user.getAvatarUrl());
                    return participantDTO;
                })
                .collect(Collectors.toSet());

        responseDTO.setParticipantUser(participantDTOs);

        return responseDTO;
    }

    @Transactional
    public ChatRoomResponseDTO handleGetChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).get();

        Set<ParticipantInfoDTO> participantDTOs = chatRoom.getParticipants().stream()
                .map(participant -> {
                    ParticipantInfoDTO participantDTO = new ParticipantInfoDTO();
                    User user = participant.getUser();
                    participantDTO.setId(user.getId());
                    participantDTO.setFirstName(user.getFirstName());
                    participantDTO.setLastName(user.getLastName());
                    participantDTO.setAvatar(user.getAvatarUrl());
                    return participantDTO;
                })
                .collect(Collectors.toSet());

        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO();
        responseDTO.setId(chatRoom.getId());
        responseDTO.setCreatedAt(chatRoom.getCreatedAt());
        responseDTO.setParticipantUser(participantDTOs);

        return responseDTO;
    }

    public ChatRoomResponseDTO handleGetChatRoomByParticipantId(Long userLoginId, Long userGuestId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByTwoUserIds(userLoginId, userGuestId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Không tìm thấy phòng chat giữa user %d và %d", userLoginId, userGuestId))
                );

        Set<ParticipantInfoDTO> participantDTOs = chatRoom.getParticipants().stream()
                .map(participant -> {
                    ParticipantInfoDTO participantDTO = new ParticipantInfoDTO();
                    User user = participant.getUser();
                    participantDTO.setId(user.getId());
                    participantDTO.setFirstName(user.getFirstName());
                    participantDTO.setLastName(user.getLastName());
                    participantDTO.setAvatar(user.getAvatarUrl());
                    return participantDTO;
                })
                .collect(Collectors.toSet());

        ChatRoomResponseDTO responseDTO = new ChatRoomResponseDTO();
        responseDTO.setId(chatRoom.getId());
        responseDTO.setCreatedAt(chatRoom.getCreatedAt());
        responseDTO.setParticipantUser(participantDTOs);

        return responseDTO;
    }

    public List<ChatRoomResponseDTO> handleGetAllChatRooms(Long userLoginId) {
        List<ChatRoom> chatRoomList = this.chatRoomRepository.findChatRoomsByUserIdWithDetails(userLoginId);
        List<ChatRoomResponseDTO> chatRoomResponseDTOList = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomResponseDTO chatRoomResponseDTO = new ChatRoomResponseDTO();
            chatRoomResponseDTO.setId(chatRoom.getId());
            chatRoomResponseDTO.setCreatedAt(chatRoom.getCreatedAt());

            Set<ParticipantInfoDTO> participantDTOs = chatRoom.getParticipants().stream()
                    .map(participant -> {
                        ParticipantInfoDTO participantDTO = new ParticipantInfoDTO();
                        User user = participant.getUser();
                        participantDTO.setId(user.getId());
                        participantDTO.setFirstName(user.getFirstName());
                        participantDTO.setLastName(user.getLastName());
                        participantDTO.setAvatar(user.getAvatarUrl());
                        return participantDTO;
                    })
                    .collect(Collectors.toSet());

            chatRoomResponseDTO.setParticipantUser(participantDTOs);

            chatRoomResponseDTOList.add(chatRoomResponseDTO);
        }

        return chatRoomResponseDTOList;
    }
}
