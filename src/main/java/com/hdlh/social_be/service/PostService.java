package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Post;
import com.hdlh.social_be.domain.PostImage;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.post.PostRequestDTO;
import com.hdlh.social_be.repository.PostRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
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

    public List<Post> handleGetPostsNewsFeed(Long lastId, int limit) {
        if (lastId == null) {
            //khi load lần đầu: fetch 5 bài mới nhất (trang thứ 0, limit bản ghi)
            return this.postRepository.findAllByOrderByIdDesc(PageRequest.of(0, limit));
        }
        //lấy bài cũ hơn (id nhỏ hơn id cuối cùng ở method trên)
        return this.postRepository.findByIdLessThanOrderByIdDesc(lastId, PageRequest.of(0, limit));
    }

    public List<Post> handleGetAllPostsByEmail(String email) {
        User user = this.userRepository.findByAccount_Email(email);
        if (user != null) {
            return this.postRepository.findAllByUser(user);
        } else {
            return null;
        }
    }

    public Post handleGetPostById(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        }
        return null;
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

    public Post handleUpdatePost(PostRequestDTO postRequestDTO, Long postId) {
        Optional<Post> postOptional = this.postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post updatedPost = postOptional.get();
            if(postRequestDTO.getContent() != null) {
                updatedPost.setContent(postRequestDTO.getContent());
            }
            if(postRequestDTO.getImageUrls() != null) {
                updatedPost.getPostImages().clear(); //xoa image cu
                for(String imageUrl : postRequestDTO.getImageUrls()) {
                    PostImage image = new PostImage();
                    image.setUrl(imageUrl);
                    image.setPost(updatedPost); // gắn lại quan hệ
                    updatedPost.getPostImages().add(image);
                }
            }
            this.postRepository.save(updatedPost);
        }
        return null;
    }

    public void handleDeletePost(Long id) {
        this.postRepository.deleteById(id);
    }
}
