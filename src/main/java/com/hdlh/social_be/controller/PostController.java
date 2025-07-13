package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.Post;
import com.hdlh.social_be.domain.dto.post.PostRequestDTO;
import com.hdlh.social_be.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequestDTO postRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequestDTO));
    }
}
