package com.hdlh.social_be.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable // Đánh dấu là một lớp có thể nhúng vào Entity khác
@Data
@NoArgsConstructor
@EqualsAndHashCode // Rất quan trọng cho các lớp khóa phức hợp
public class ChatRoomParticipantId implements Serializable {
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "user_id")
    private Long userId;

    public ChatRoomParticipantId(Long roomId, Long userId) {
        this.roomId = roomId;
        this.userId = userId;
    }
}
