package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//Tự động tạo pattern Builder giúp bạn khởi tạo đối tượng linh hoạt hơn, nhất là khi có nhiều field.
//User user = User.builder()
//    .email("john@example.com")
//    .password("hashed_password")
//    .fullName("John Doe")
//    .build();
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

}
