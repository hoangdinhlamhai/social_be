package com.hdlh.social_be.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

//bảng trung gian giữa User và Company
@Entity
@Table(name = "experiences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrent;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id") // khoá ngoại
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id") // khoá ngoại
    private Company company;
}
