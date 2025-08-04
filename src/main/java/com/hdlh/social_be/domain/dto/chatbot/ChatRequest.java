package com.hdlh.social_be.domain.dto.chatbot;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatRequest {
    private String message;
}
