package com.hdlh.social_be.domain.dto;

import lombok.Data;

//server trả về client
@Data
public class ProfileResponseDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String coverPhotoUrl;
    private String bio;

    private Long cityId;
    private String cityName;

    private Long countryId;
    private String countryName;
}

