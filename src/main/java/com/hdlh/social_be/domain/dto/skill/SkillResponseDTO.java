package com.hdlh.social_be.domain.dto.skill;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SkillResponseDTO {
    private Long id;
    private String skillName;
    private String skillDescription;
}
