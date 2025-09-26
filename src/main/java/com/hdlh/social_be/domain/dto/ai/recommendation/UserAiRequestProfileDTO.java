package com.hdlh.social_be.domain.dto.ai.recommendation;

import java.util.List;

public record UserAiRequestProfileDTO(Long id, String email, String name, String avatar_url, String bio, List<String> skills) {}
