package com.hdlh.social_be.domain.dto.post;

import lombok.Data;

import java.util.List;

@Data
public class PostRequestDTO {
    private String content;
    private List<String> imageUrls;
}
