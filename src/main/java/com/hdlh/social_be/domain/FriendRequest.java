package com.hdlh.social_be.domain;

import com.hdlh.social_be.domain.enums.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
}
