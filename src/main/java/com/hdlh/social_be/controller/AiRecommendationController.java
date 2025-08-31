package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.ai.recommendation.UserAiRequestProfileDTO;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/custom-ai-suggested")
    public Mono<ResponseEntity<List<UserAiRequestProfileDTO>>> getCustomAiSuggestions(@RequestParam Long userId) {
        // 1. Gọi service để lấy Mono<List<User>>
        Mono<List<UserAiRequestProfileDTO>> recommendedUsersMono = aiRecommendationService.getCustomAiRecommendations(userId);

        // 2. Dùng `map` để biến đổi kết quả thành ResponseEntity khi nó đến
        //    và dùng `defaultIfEmpty` để xử lý trường hợp không có kết quả
        return recommendedUsersMono
                .map(userList -> ResponseEntity.ok(userList))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
