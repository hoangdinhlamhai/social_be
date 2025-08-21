package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Post;
import com.hdlh.social_be.domain.PostImage;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.post.PostRequestDTO;
import com.hdlh.social_be.repository.PostRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> handleGetAllPostsByEmail(String email) {
        User user = this.userRepository.findByAccount_Email(email);
        if (user != null) {
            return this.postRepository.findAllByUser(user);
        } else {
            return null;
        }
    }

    public Post createPost(PostRequestDTO postRequestDTO, String email) {
        //lấy user đang login
        User currentUser = userRepository.findByAccount_Email(email);

        Post post = new Post();
        post.setContent(postRequestDTO.getContent());
        post.setUser(currentUser);

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
