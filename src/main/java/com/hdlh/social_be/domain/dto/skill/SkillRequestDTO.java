package com.hdlh.social_be.domain.dto.skill;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SkillRequestDTO {
    @NotBlank @Size(max = 255)
    private String skillName;
    @Size(max = 5000)
    private String skillDescription;
}
