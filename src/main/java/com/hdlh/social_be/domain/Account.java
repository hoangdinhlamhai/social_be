package com.hdlh.social_be.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hdlh.social_be.domain.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "accounts")
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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private AccountType accountType; // PERSONAL, COMPANY

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    private User user;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Company company;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

}
