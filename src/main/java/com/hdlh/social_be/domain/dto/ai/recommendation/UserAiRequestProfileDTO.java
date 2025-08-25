package com.hdlh.social_be.domain.dto.ai.recommendation;

import java.util.List;

public record UserAiRequestProfileDTO(Long id, String name, List<String> skills) {}
