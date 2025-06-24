package com.hdlh.social_be.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//để định dạng lại token trả về
public class ResponseLoginDTO {
    private String accessToken;
    private AccountLogin accountLogin;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountLogin {
        private long id;
        private String email, password;
    }
}
