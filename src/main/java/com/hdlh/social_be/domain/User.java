package com.hdlh.social_be.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    //@JsonBackReference //khi serialize Account, nó sẽ include User,
    @JsonIgnore
    // nhưng khi đi ngược lại từ User về Account, nó bỏ qua để tránh lặp.
    private Account account;

    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String coverPhotoUrl;
    private String bio;

    //cascade = CascadeType.ALL:
    //Nếu lưu/xoá UserProfile, thì các skills, experiences,... cũng tự động lưu/xoá theo.
    //orphanRemoval = true:
    //Nếu xoá một Skill khỏi danh sách skills, nó sẽ tự động bị xoá khỏi database (nếu không còn liên kết).
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Skill> skills = new ArrayList<>();

    //bên class kia phải có trường user (và ở table bên kia phải có khoá ngoại user_id)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Education> educations = new ArrayList<>();

    //bên class kia phải có trường user (và ở table bên kia phải có khoá ngoại user_id)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore //bỏ qua trường này khi gọi user để tránh lặp (user -> post, trong post lại gọi user -> post)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore //thêm cái này tránh lỗi lặp vô hạn ghi gán user cho FriendRequest, trong User lại có sentFriendRequest
    private List<FriendRequest> sentFriendRequest = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<FriendRequest> receivedFriendRequest = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "city_id")
//    @JsonBackReference
    @JsonIgnore
    private City city;

}
