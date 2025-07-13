package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Post;
import com.hdlh.social_be.domain.PostImage;
import com.hdlh.social_be.domain.dto.post.PostRequestDTO;
import com.hdlh.social_be.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(PostRequestDTO postRequestDTO) {
        Post post = new Post();
        post.setContent(postRequestDTO.getContent());

        if(postRequestDTO.getImageUrls() != null) {
            for (String imageUrl : postRequestDTO.getImageUrls()) {
                PostImage image = new PostImage();
                image.setUrl(imageUrl);

                image.setPost(post); // 1. Set tham chiếu ngược từ ImagePost về Post
                post.getPostImages().add(image); // 2. Thêm ImagePost vào danh sách của Post
            }
        }

        return postRepository.save(post);
    }
}
