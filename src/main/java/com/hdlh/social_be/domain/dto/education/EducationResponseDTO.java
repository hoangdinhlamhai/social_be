package com.hdlh.social_be.domain.dto.education;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EducationResponseDTO {
    private Long id;
    private String schoolName;
    private String degree;
    private String fieldOfStudy;
    private String className;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    private String description;
}
