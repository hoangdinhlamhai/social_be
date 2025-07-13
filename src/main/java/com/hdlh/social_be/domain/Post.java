package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;
    private String createAt;
    private String visibility;

    @ManyToOne
    @JoinColumn(name = "user_id") // khoá ngoại
    private User user;

    //bên class kia phải có trường post (và ở table bên kia phải có khoá ngoại post_id)
    @OneToMany(
            mappedBy = "post",
            cascade = CascadeType.ALL, // Lưu, cập nhật, xóa ImagePost cùng với Post
            orphanRemoval = true // Xóa ImagePost nếu nó không còn liên kết với Post nào
    )
    private List<PostImage> postImages = new ArrayList<>();
}
