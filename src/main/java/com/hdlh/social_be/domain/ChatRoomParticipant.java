package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room_participants")
@Data
@NoArgsConstructor
public class ChatRoomParticipant {
    @EmbeddedId // Đánh dấu ID là một lớp được nhúng
    private ChatRoomParticipantId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomId") // Ánh xạ thuộc tính `roomId` trong `ChatRoomParticipantId`
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // Ánh xạ thuộc tính `userId` trong `ChatRoomParticipantId`
    @JoinColumn(name = "user_id")
    private User user;

    public ChatRoomParticipant(ChatRoom room, User user) {
        this.room = room;
        this.user = user;
        this.id = new ChatRoomParticipantId(room.getId(), user.getId());
    }
}
