package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    @EntityGraph(attributePaths = {"city", "city.country"})
    User findByAccount_Email(String accountEmail);
}
