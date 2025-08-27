package com.hdlh.social_be.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class ChatbotService {

    @Value("${gemini.api.chatbot.key}")
    private String apiChatbotKey;

    public String askGemini(String userMessage) {
        try {
            URL url = new URL("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiChatbotKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            //Dùng đúng cấu trúc body của Gemini
            String body = """
        {
          "contents": [{
            "parts":[{
              "text": "%s"
            }]
          }]
        }
        """.formatted(userMessage.replace("\"", "\\\""));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            // Kiểm tra mã phản hồi trước khi đọc
            if (conn.getResponseCode() != 200) {
                // Đọc luồng lỗi để biết chi tiết
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }
                throw new RuntimeException("Lỗi từ server: " + conn.getResponseCode() + " - " + response.toString());
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            // Trích nội dung từ JSON response của Gemini
            String fullResponse = response.toString();
            JSONObject json = new JSONObject(fullResponse);
            String content = json.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi gọi Gemini: " + e.getMessage();
        }
    }

}
