package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String bio;

    //cascade = CascadeType.ALL:
    //Nếu lưu/xoá UserProfile, thì các skills, experiences,... cũng tự động lưu/xoá theo.
    //orphanRemoval = true:
    //Nếu xoá một Skill khỏi danh sách skills, nó sẽ tự động bị xoá khỏi database (nếu không còn liên kết).
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills = new ArrayList<>();

    //bên class kia phải có trường user (và ở table bên kia phải có khoá ngoại user_id)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

}
