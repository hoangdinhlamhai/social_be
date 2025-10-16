package com.hdlh.social_be.domain.dto.friend_request;

import com.hdlh.social_be.domain.enums.Status;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestResponseDTO {
    private Long id;
    private Status status;
    private UserInfo sender;
    private UserInfo receiver;

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String avatarUrl;
        private String coverPhotoUrl;
        private String bio;
    }
}
