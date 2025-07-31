package com.hdlh.social_be.domain.dto.OAuth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleLoginDTO {
    private String email;
    private String family_name;
    private String given_name;
    private String picture;
    private String name;
}
