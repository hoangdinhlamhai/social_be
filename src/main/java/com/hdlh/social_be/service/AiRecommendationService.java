package com.hdlh.social_be.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdlh.social_be.domain.dto.ai.recommendation.GeminiResponse;
import com.hdlh.social_be.domain.dto.ai.recommendation.UserAiRequestProfileDTO;
import com.hdlh.social_be.mapper.UserMapper;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.hdlh.social_be.domain.User;

import java.util.List;
import java.util.Map;

@Service
public class AiRecommendationService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper; // Dùng để chuyển đổi object sang JSON
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    public AiRecommendationService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper, UserRepository userRepository, UserMapper userMapper) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Mono<String> getRecommendations(UserAiRequestProfileDTO currentUser, List<UserAiRequestProfileDTO> candidates) {
        String prompt = buildPrompt(currentUser, candidates);
        Map<String, Object> requestBody = buildRequestBody(prompt);
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

        return webClient.post()
                .uri(apiUrl, uriBuilder -> uriBuilder.queryParam("key", apiKey).build())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(GeminiResponse.class) // Map thẳng response vào DTO
                .map(geminiResponse -> {
                    // Trích xuất nội dung text từ cấu trúc JSON phức tạp
                    if (geminiResponse != null && geminiResponse.candidates() != null && !geminiResponse.candidates().isEmpty()) {
                        return geminiResponse.candidates().get(0).content().parts().get(0).text();
                    }
                    return "{\"error\": \"Không nhận được phản hồi hợp lệ từ AI.\"}";
                })
                .doOnError(error -> System.err.println("Lỗi khi gọi Gemini API: " + error.getMessage()));
    }

    private String buildPrompt(UserAiRequestProfileDTO currentUser, List<UserAiRequestProfileDTO> candidates) {
        try {
            // Chuyển đổi đối tượng Java thành chuỗi JSON để đưa vào prompt
            String currentUserJson = objectMapper.writeValueAsString(currentUser);
            String candidatesJson = objectMapper.writeValueAsString(candidates);

            // Đây là prompt template, bạn có thể chỉnh sửa để có kết quả tốt hơn
            return String.format("""
                Bạn là một chuyên gia tư vấn mạng lưới nhân sự. Dựa vào hồ sơ của người dùng hiện tại và danh sách ứng viên, hãy đề xuất 5 người phù hợp nhất để kết nối.

                Hồ sơ người dùng hiện tại:
                %s

                Danh sách ứng viên để xem xét:
                %s

                Hãy trả về kết quả dưới dạng một chuỗi JSON hợp lệ chứa một mảng các đối tượng. Mỗi đối tượng phải có các trường "id", "name", và một trường "reason" giải thích ngắn gọn tại sao người đó được gợi ý.
                Chỉ trả về chuỗi JSON, không thêm bất kỳ văn bản nào khác.
                """, currentUserJson, candidatesJson);
        } catch (Exception e) {
            // Xử lý lỗi khi convert JSON
            throw new RuntimeException("Lỗi khi xây dựng prompt", e);
        }
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        // Xây dựng body của request theo đúng định dạng Gemini yêu cầu
        Map<String, String> textPart = Map.of("text", prompt);
        Map<String, List<Map<String, String>>> content = Map.of("parts", List.of(textPart));
        Map<String, Object> body = Map.of("contents", List.of(content));
        return body;
    }

    public Mono<String> getAiRecommendationsForUser(Long userId) {
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<User> allUsers = userRepository.findAll();

        UserAiRequestProfileDTO currentUserDto = userMapper.toAiRequestProfileDTO(currentUser);

        List<User> candidates = allUsers.stream()
                .filter(u -> !u.getId().equals(userId))
                .toList();
        List<UserAiRequestProfileDTO> candidateDtos = userMapper.toAiProfileDtoList(candidates);

        return this.getRecommendations(currentUserDto, candidateDtos);
    }
}
