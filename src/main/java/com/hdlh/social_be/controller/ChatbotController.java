package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.chatbot.ChatRequest;
import com.hdlh.social_be.domain.dto.chatbot.ChatResponse;
import com.hdlh.social_be.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatbotController {
    private ChatbotService chatbotService;

    public ChatbotController(ChatbotService gptService) {
        this.chatbotService = gptService;
    }

    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = chatbotService.askGemini(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
