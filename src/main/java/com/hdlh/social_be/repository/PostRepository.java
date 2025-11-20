package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.Post;
import com.hdlh.social_be.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByIdDesc(Pageable pageable);
    List<Post> findByIdLessThanOrderByIdDesc(Long lastId, Pageable pageable);
    public List<Post> findAllByUser(User user);
}
