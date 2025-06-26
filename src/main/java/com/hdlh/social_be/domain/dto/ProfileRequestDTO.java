package com.hdlh.social_be.domain.dto;

import lombok.Data;

@Data
public class ProfileRequestDTO {
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String coverPhotoUrl;
    private String bio;
}
