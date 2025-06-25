package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.Account;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.LoginDTO;
import com.hdlh.social_be.domain.dto.RegisterDTO;
import com.hdlh.social_be.domain.dto.ResponseLoginDTO;
import com.hdlh.social_be.domain.enums.AccountType;
import com.hdlh.social_be.service.AccountService;
import com.hdlh.social_be.service.UserService;
import com.hdlh.social_be.util.SecurityUtil;
import com.hdlh.social_be.util.error.IdInvalidException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Value("${social.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil, AccountService accountService, PasswordEncoder passwordEncoder) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        if(accountService.checkIfAccountExists(registerDTO.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        // userRepository.save(user); // có thể bỏ nếu dùng cascade ở Account

        Account account = new Account();
        account.setEmail(registerDTO.getEmail());
        account.setPassword(passwordEncoder.encode(registerDTO.getPassword())); // mã hóa!
        account.setAccountType(AccountType.valueOf(registerDTO.getAccountType()));
        account.setUser(user); // gán user vào account
        user.setAccount(account); // gán account vào user

        return ResponseEntity.ok(accountService.handleSaveAccount(account));
    }

    //access token trả về từ api cho fe tự lưu
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
        Account currentAccountDB = this.accountService.handleGetAccountByUsername(loginDTO.getEmail());
        if (currentAccountDB != null) {
            ResponseLoginDTO.AccountLogin accountLogin = new ResponseLoginDTO.AccountLogin(currentAccountDB.getId(), currentAccountDB.getEmail(), currentAccountDB.getPassword());
            res.setAccountLogin(accountLogin);
        }
        res.setAccessToken(access_token);

        //create refresh_token
        String refresh_token = this.securityUtil.createRefreshToken(currentAccountDB.getEmail(), res);

        //lưu refresh_token
        this.accountService.updateRefreshTokenAccount(refresh_token, loginDTO.getEmail());

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

    //có 2 cách xác thực, access token (jwt) và refresh token
    //dùng refresh_token để xác thực endpoint này, tức là không cần gửi access_token vào header
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {

        // Lấy cookie refresh_token
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No cookies found!");
        }

        String refreshToken = null;
        for (Cookie cookie : cookies) {
            if ("refresh_token".equals(cookie.getName())) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token not found!");
        }

        // Nếu có thì tìm user theo token & xoá refresh_token khỏi DB
        String email = securityUtil.getUsernameFromToken(refreshToken); // hoặc getSubject
        accountService.clearRefreshToken(email); // set refresh_token = null trong DB

        // Tạo cookie rỗng để xoá ở phía client
        ResponseCookie clearCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(Map.of("message", "Đăng xuất thành công"));
    }

    @GetMapping("auth/refresh")
    //@ApiMessage("Get User by refresh token")
    public ResponseEntity<ResponseLoginDTO> refresh(
            @CookieValue(name = "refresh_token") String refresh_token
    ) throws IdInvalidException {
        //check valid refresh_token có đúng không
        Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = decodedToken.getSubject(); //lấy email dựa trên refresh token

        //check xem có refresh_token ở db ko
        Account currentAccount = this.accountService.getUserByRefreshTokenAndEmail(refresh_token, email);
        if (currentAccount == null) {
            throw new IdInvalidException("Refresh token khong hop le");
        }

        //return ResponseEntity.ok().body(refresh_token); //trả ra token khi gọi api này

        ResponseLoginDTO res = new ResponseLoginDTO();

        //API Login
        Account currentAccountDB = this.accountService.handleGetAccountByUsername(email);
        if (currentAccountDB != null) {
            ResponseLoginDTO.AccountLogin accountLogin = new ResponseLoginDTO.AccountLogin(currentAccountDB.getId(), currentAccountDB.getEmail(), currentAccountDB.getPassword());
            res.setAccountLogin(accountLogin);
        }
//        res.setAccessToken(access_token);

        //create refresh token
        String new_refreshToken = this.securityUtil.createRefreshToken(email, res);

        //update refresh_token
        this.accountService.updateRefreshTokenAccount(new_refreshToken, email);

        //set cookie
        ResponseCookie resCookie = ResponseCookie.from("refresh_token", new_refreshToken)
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
