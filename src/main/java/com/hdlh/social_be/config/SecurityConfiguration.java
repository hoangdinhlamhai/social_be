package com.hdlh.social_be.config;

import com.hdlh.social_be.auth.GoogleAuthenticationProvider;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${social.jwt.base64-secret}")
    private String jwtKey;

    private static final String ALGORITHM = "HmacSHA256";

    //Cấu hình AuthenticationManagerBuilder để thêm provider (dùng authentication không cần xác minh password)
    //bị lỗi vòng lặp rồi
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth,
//                                UserDetailsService userDetailsService,
//                                PasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
//
//        daoProvider.setUserDetailsService(userDetailsService);
//        daoProvider.setPasswordEncoder(passwordEncoder);
//
//        auth.authenticationProvider(daoProvider);             // login thường (có xác minh password)
//    }

    // cấu hình AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, GoogleAuthenticationProvider googleAuthProvider) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(daoProvider, googleAuthProvider);
    }


    //khai báo hashpassword (gọi trong controller để dùng)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //cấu hình như này mặc dù /logout nằm trong .permitAll nhưng /logout có xử lý token nên vẫn dính lỗi 401 nên phải tách riêng như dưới
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .cors(Customizer.withDefaults())  // gọi cors(ở đây là cấu hình mặc định)
//                .csrf(c -> c.disable())
//                .authorizeHttpRequests(authorize -> authorize
////                        .requestMatchers("/messages/**").access(hasScope("message:read"))
//                        .requestMatchers("/login", "/logout", "/users").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())); // xác thực bằng JWT (access_token)
//        return http.build();
//    }

    @Bean
    @Order(1)
    public SecurityFilterChain permitAllFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())  // gọi cors(ở đây là cấu hình mặc định)
                .securityMatcher("/login", "/logout", "/register", "/check-email", "/send-otp", "/verify-otp", "/change-password")
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .logout().disable() // <-- Vô hiệu hoá /logout mặc định
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .securityMatcher("/api/**") // chỉ áp dụng JWT cho /api/*
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }


    //giải mã token
    @Bean
    public JwtDecoder jwtDecoder(SecretKey key) {
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    // tự sinh token, không cần nếu token được lấy từ server khác
    @Bean
    public JwtEncoder jwtEncoder(SecretKey key) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    //ký và giải mã JWT dùng thuật toán HS256
    @Bean
    public SecretKey jwtSecretKey() {
        byte[] decodedKey = Base64.from(jwtKey).decode();
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }
    //cần thêm bước sinh jwt trong authcontroller


}
