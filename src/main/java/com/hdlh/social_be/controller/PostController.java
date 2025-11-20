package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.Post;
import com.hdlh.social_be.domain.dto.post.PostRequestDTO;
import com.hdlh.social_be.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/newsfeed")
    public ResponseEntity<List<Post>> getAllPostsNewsFeed(
                                        @RequestParam(required = false) Long lastId,
                                        @RequestParam(defaultValue = "5") int limit
                                        ) {
        return ResponseEntity.ok(this.postService.handleGetPostsNewsFeed(lastId, limit));
    }

    @GetMapping("")
    public ResponseEntity<List<Post>> getAllPostsByEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.OK).body(this.postService.handleGetAllPostsByEmail(email));
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequestDTO postRequestDTO
                                           //@AuthenticationPrincipal UserDetails userDetails // Lấy user đã xác thực
    )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // hoặc getPrincipal()
        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (email == null) {
            // Trả về lỗi 401 Unauthorized nếu không có user nào được xác thực
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.postService.createPost(postRequestDTO, email));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<Post> updatePost(@RequestBody PostRequestDTO postRequestDTO, @PathVariable Long postId) {
        return ResponseEntity.ok(this.postService.handleUpdatePost(postRequestDTO, postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable("postId") Long postId) {
        this.postService.handleDeletePost(postId);
        return ResponseEntity.ok(null);
    }
}
