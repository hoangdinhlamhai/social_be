package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.LoginDTO;
import com.hdlh.social_be.domain.dto.ResponseLoginDTO;
import com.hdlh.social_be.service.UserService;
import com.hdlh.social_be.util.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${social.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        // nap input username va password cho Spring Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword());

        // xac thuc nguoi dung => can ghi de lai ham loadUserByUserName
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // đã lấy đc th tin người dùng đnhập thành công

        // create a token
        // do ở file FormatRestResponse.java cấu hình yêu cầu trả về 1 Obj nên phải tạo
        // ResponseLoginDTO
        String access_token = this.securityUtil.createAccessToken(authentication.getName());

        // tạo token xong thì xét data cho SecurityContext, sau này lấy data từ đây (dự án này thì xoá đi cũng được)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseLoginDTO res = new ResponseLoginDTO();

        //API Login
        User currentUserDB = this.userService.handleGetUserByUsername(loginDTO.getEmail());
        if (currentUserDB != null) {
            ResponseLoginDTO.UserLogin userLogin = new ResponseLoginDTO.UserLogin(currentUserDB.getId(), currentUserDB.getEmail(), currentUserDB.getPassword());
            res.setUser(userLogin);
        }
        res.setAccessToken(access_token);

        //create refresh_token
        String refresh_token = this.securityUtil.createRefreshToken(currentUserDB.getEmail(), res);

        //lưu refresh_token cho user
        this.userService.updateRefreshTokenUser(refresh_token, loginDTO.getEmail());

        //set cookie
        ResponseCookie resCookie = ResponseCookie.from("refresh_token", refresh_token)
                .httpOnly(true) //chi cho server dung
                .secure(true) //chi dung cho https
                .path("/")
                .maxAge(refreshTokenExpiration) //thoi gian het han
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookie.toString())
                .body(res);
    }
}
