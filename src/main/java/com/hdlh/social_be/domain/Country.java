package com.hdlh.social_be.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "countries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference //tránh lặp vô hạn khi gọi api
    private List<City> cities = new ArrayList<>();
}

