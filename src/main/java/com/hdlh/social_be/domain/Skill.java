package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String skillName;
    private String skillDescription;

    @ManyToOne
    @JoinColumn(name = "user_id") // khoá ngoại
    private User user;
}
