package com.hdlh.social_be.util;

import com.hdlh.social_be.domain.dto.ResponseLoginDTO;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUtil {

    @Value("${social.jwt.base64-secret}")
    private String jwtKey;

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${social.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${social.jwt.access-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public static final MacAlgorithm JW_ALGORITHM = MacAlgorithm.HS256;

    public SecurityUtil(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode(); // lấy key từ biến môi trường để mã hoá ngược lại
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JW_ALGORITHM.getName());
    }

    public String createAccessToken(String email) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        //hardcode permission (for testing)
        List<String> listAuthority = new ArrayList<String>();
        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", email) //token se chua thong tin o claim
                .claim("permission", listAuthority) //token se chua thong tin o claim
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JW_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public String createRefreshToken(String email, ResponseLoginDTO responseLoginDTO) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("heri", responseLoginDTO.getAccountLogin())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JW_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    //api logout
    public String getUsernameFromToken(String token) {
        //giải mã
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject(); // Subject chính là email
    }

    //
    public Jwt checkValidRefreshToken(String token) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtil.JW_ALGORITHM)
                .build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> JWT error: " + e.getMessage());
            throw e;
        }
    }

}
