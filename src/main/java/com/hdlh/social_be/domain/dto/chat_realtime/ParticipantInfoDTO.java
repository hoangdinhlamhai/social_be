package com.hdlh.social_be.domain.dto.chat_realtime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantInfoDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String avatar;
}
