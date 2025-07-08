package com.hdlh.social_be.service;

import com.hdlh.social_be.adapter.CloudinaryAdapter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CloudinaryAdapterImpl implements CloudinaryAdapter {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecret;

    @Override
    public String uploadImage(MultipartFile file, String folder) {
        try {
            long timestamp = System.currentTimeMillis() / 1000;

            Map<String, Object> params = new HashMap<>();
            params.put("timestamp", timestamp);
            params.put("folder", folder);

            String signature = generateSignature(params, apiSecret);

            HttpPost post = new HttpPost("https://api.cloudinary.com/v1_1/" + cloudName + "/image/upload");

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addPart("file", new ByteArrayBody(file.getBytes(), ContentType.DEFAULT_BINARY, file.getOriginalFilename()));
            builder.addTextBody("api_key", apiKey);
            builder.addTextBody("timestamp", String.valueOf(timestamp));
            builder.addTextBody("signature", signature);
            builder.addTextBody("folder", folder);

            HttpEntity entity = builder.build();
            post.setEntity(entity);

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {

                String json = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(json);
                return jsonObject.getString("secure_url");
            }

        } catch (Exception e) {
            throw new RuntimeException("Upload to Cloudinary failed", e);
        }
    }

    private String generateSignature(Map<String, Object> params, String apiSecret) {
        String toSign = params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        return DigestUtils.sha1Hex(toSign + apiSecret);
    }
}
