package com.hdlh.social_be.domain.dto.chatbot;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatResponse {
    private String reply;

    public ChatResponse(String reply) {
        this.reply = reply;
    }
}
