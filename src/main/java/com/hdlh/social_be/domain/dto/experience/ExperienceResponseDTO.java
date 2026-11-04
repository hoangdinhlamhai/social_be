package com.hdlh.social_be.domain.dto.experience;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExperienceResponseDTO {
    private Long id;
    private String title;
    private String companyName;
    private String employmentType;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    private String description;
}
