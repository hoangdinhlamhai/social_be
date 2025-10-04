package com.hdlh.social_be.domain.dto.friend_request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FriendRequestDTO {
    private Long senderId;
    private Long receiverId;
}
