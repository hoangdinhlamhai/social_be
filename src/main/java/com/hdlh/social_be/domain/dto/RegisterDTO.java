package com.hdlh.social_be.domain.dto;

import lombok.Data;

//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@RequiredArgsConstructor
// = Data
@Data
public class RegisterDTO {
    private String email;
    private String password;
    private String accountType;
    private String firstName;
    private String lastName;
}
