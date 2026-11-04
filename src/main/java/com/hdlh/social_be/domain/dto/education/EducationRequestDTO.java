package com.hdlh.social_be.domain.dto.education;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class EducationRequestDTO {
    @NotBlank @Size(max = 255)
    private String schoolName;
    @Size(max = 255)
    private String degree;
    @Size(max = 255)
    private String fieldOfStudy;
    @Size(max = 255)
    private String className;
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    @Size(max = 5000)
    private String description;
}
