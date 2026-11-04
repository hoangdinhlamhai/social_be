package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.Experience;
import com.hdlh.social_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findAllByUserOrderByIsCurrentDescStartDateDescIdDesc(User user);
}
