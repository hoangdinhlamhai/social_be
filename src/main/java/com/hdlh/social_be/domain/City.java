package com.hdlh.social_be.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonBackReference
    private Country country;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
}

