package com.hdlh.social_be.domain.dto.ai.recommendation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Bỏ đi class GeminiResponse bên ngoài, chỉ giữ lại record chính
@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiResponse(List<Candidate> candidates) {

    // Các record phụ được định nghĩa là public static bên trong record chính
    // Điều này giúp nhóm chúng lại với nhau một cách logic

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Candidate(Content content) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Content(List<Part> parts) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Part(String text) {}
}