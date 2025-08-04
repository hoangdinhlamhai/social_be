package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.chatbot.ChatRequest;
import com.hdlh.social_be.domain.dto.chatbot.ChatResponse;
import com.hdlh.social_be.service.GptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatbotController {
    private GptService gptService;

    public ChatbotController(GptService gptService) {
        this.gptService = gptService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String reply = gptService.askGpt(request.getMessage());
        return ResponseEntity.ok(new ChatResponse(reply));
    }
}
