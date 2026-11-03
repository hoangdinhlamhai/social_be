package com.hdlh.social_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdlh.social_be.controller.PostController;
import com.hdlh.social_be.domain.Post;
import com.hdlh.social_be.domain.PostImage;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.repository.PostRepository;
import com.hdlh.social_be.repository.UserRepository;
import org.hibernate.collection.spi.PersistentBag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostServiceTests {
    private final PostRepository postRepository = mock(PostRepository.class);
    private final PostService postService = new PostService(postRepository, mock(UserRepository.class));
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = 12L)
    void newsfeedCanBeSerializedWithoutAccessingEntities(Long lastId) throws Exception {
        User user = new User();
        user.setId(3L);
        user.setFirstName("Hai");
        user.setLastName("Hoang");
        user.setAvatarUrl("avatar.jpg");
        user.setExperiences(new PersistentBag<>());

        Post post = new Post();
        post.setId(11L);
        post.setContent("Newsfeed post");
        post.setCreateAt(LocalDateTime.of(2026, 11, 3, 15, 47));
        post.setVisibility("PUBLIC");
        post.setUser(user);

        PostImage image = new PostImage();
        image.setId(7L);
        image.setUrl("post.jpg");
        image.setPost(post);
        post.getPostImages().add(image);

        if (lastId == null) {
            when(postRepository.findAllByOrderByIdDesc(PageRequest.of(0, 5))).thenReturn(List.of(post));
        } else {
            when(postRepository.findByIdLessThanOrderByIdDesc(lastId, PageRequest.of(0, 5)))
                    .thenReturn(List.of(post));
        }

        Object response = new PostController(postService).getAllPostsNewsFeed(lastId, 5).getBody();
        // Sau khi service trả về, response không được phụ thuộc vào entity/session nữa.
        post.setPostImages(new PersistentBag<>());
        user.setFirstName("Changed after service returned");

        JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(response));
        assertThat(json.size()).isEqualTo(1);
        assertThat(json.get(0).get("id").asLong()).isEqualTo(11L);
        assertThat(json.get(0).get("content").asText()).isEqualTo("Newsfeed post");
        assertThat(json.get(0).get("createAt").asText()).isEqualTo("2026-11-03 03:47:00 PM");
        assertThat(json.get(0).get("visibility").asText()).isEqualTo("PUBLIC");
        assertThat(json.get(0).get("user").get("firstName").asText()).isEqualTo("Hai");
        assertThat(json.get(0).get("user").get("avatarUrl").asText()).isEqualTo("avatar.jpg");
        assertThat(json.get(0).get("user").has("experiences")).isFalse();
        assertThat(json.get(0).get("postImages").get(0).get("id").asLong()).isEqualTo(7L);
        assertThat(json.get(0).get("postImages").get(0).get("url").asText()).isEqualTo("post.jpg");
        assertThat(json.get(0).get("postImages").get(0).has("post")).isFalse();
    }

    @Test
    void newsfeedReturnsEmptyArrayWhenNoMorePosts() throws Exception {
        when(postRepository.findByIdLessThanOrderByIdDesc(1L, PageRequest.of(0, 5))).thenReturn(List.of());

        assertThat(objectMapper.writeValueAsString(postService.handleGetPostsNewsFeed(1L, 5)))
                .isEqualTo("[]");
    }
}
