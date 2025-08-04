package com.hdlh.social_be.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class GptService {

    public String askGpt(String userMessage) {
        try {
            URL url = new URL("https://openrouter.ai/api/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Authorization", "Bearer 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");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String body = """
        {
          "model": "qwen/qwen3-coder:free", 
          "messages": [
            { "role": "user", "content": "%s" }
          ]
        }
        """.formatted(userMessage.replace("\"", "\\\""));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            // Trích nội dung từ JSON trả về
            String full = response.toString();
            JSONObject json = new JSONObject(full);
            String content = json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi gọi GPT proxy: " + e.getMessage();
        }
    }

}
