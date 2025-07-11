package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id") // khoá ngoại
    private Post post;
}
