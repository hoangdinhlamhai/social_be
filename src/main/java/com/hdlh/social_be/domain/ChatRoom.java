package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

    @Entity
    @Table(name = "chat_rooms")
    //Khi JPA/Hibernate thực hiện save, sẽ gọi đến các equals() và hashCode() trên các Entity để quản lý trạng thái trong "persistence context".
    //ChatRoom.hashCode() -> gọi participants.hashCode() -> gọi ChatRoomParticipant.hashCode() -> gọi chatRoom.hashCode() -> đệ quy vô hạn
    // bỏ =>   @Data
    @Getter
    @Setter
    @NoArgsConstructor
    public class ChatRoom {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @CreationTimestamp // Tự động gán thời gian khi tạo mới
        @Column(name = "created_at", nullable = false, updatable = false)
        private LocalDateTime createdAt;

        // Dùng Set để đảm bảo mỗi người dùng chỉ tham gia một lần
        @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
        private Set<ChatRoomParticipant> participants = new HashSet<>();

        @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
        private List<Message> messages;
    }
