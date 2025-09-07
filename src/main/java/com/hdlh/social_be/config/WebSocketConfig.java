package com.hdlh.social_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Các tin nhắn có đích đến bắt đầu bằng "/app" sẽ được định tuyến đến các phương thức @MessageMapping trong controller
        registry.setApplicationDestinationPrefixes("/app");
        // Bật một message broker đơn giản trong bộ nhớ
        // Các tin nhắn sẽ được broadcast đến tất cả client đang subscribe vào các topic bắt đầu bằng "/topic"
        registry.enableSimpleBroker("/topic");
    }
}
