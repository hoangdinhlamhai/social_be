package com.hdlh.social_be.controller;

import org.springframework.web.bind.annotation.*;
import com.hdlh.social_be.service.AiRecommendationService;
import com.hdlh.social_be.domain.User;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class AiRecommendationController {
    private final AiRecommendationService aiRecommendationService;

    public AiRecommendationController(AiRecommendationService aiRecommendationService) {
        this.aiRecommendationService = aiRecommendationService;
    }

    @GetMapping("/ai-suggested")
    public Mono<String> getAiSuggestions(@RequestParam Long userId) {
        return aiRecommendationService.getAiRecommendationsForUser(userId);
    }
}
