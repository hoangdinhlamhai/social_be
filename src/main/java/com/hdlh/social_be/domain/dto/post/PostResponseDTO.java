package com.hdlh.social_be.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a", timezone = "GMT+7")
    private LocalDateTime createAt;

    private String visibility;
    private UserInfo user;
    private List<PostImageInfo> postImages;

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String avatarUrl;
        private String coverPhotoUrl;
        private String bio;
    }

    @Getter @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostImageInfo {
        private Long id;
        private String url;
    }
}
