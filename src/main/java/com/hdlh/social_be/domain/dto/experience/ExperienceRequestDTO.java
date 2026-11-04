package com.hdlh.social_be.domain.dto.experience;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ExperienceRequestDTO {
    @NotBlank @Size(max = 255)
    private String title;
    @NotBlank @Size(max = 255)
    private String companyName;
    @Size(max = 255)
    private String employmentType;
    @Size(max = 255)
    private String location;
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    @Size(max = 5000)
    private String description;
}
