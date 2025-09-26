package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    @EntityGraph(attributePaths = {"city", "city.country"})
    User findByAccount_Email(String accountEmail);

    //ở phần ai tự train trong AiRecommendationService có lỗi:
    //failed to lazily initialize a collection of role: com.hdlh.social_be.domain.User.skills: could not initialize proxy - no Session
    //do user.getSkills() là collection LAZY, nhưng bạn gọi nó ở ngoài Session (sau khi Hibernate đã đóng session).
    //Khi lấy usersFromDB, Hibernate fetch luôn skills, mapper không bị lỗi.
    @EntityGraph(attributePaths = {"skills", "account"})
    List<User> findAllById(Iterable<Long> ids);

}
