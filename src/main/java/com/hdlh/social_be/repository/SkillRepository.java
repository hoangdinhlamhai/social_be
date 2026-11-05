package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.Skill;
import com.hdlh.social_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByUserOrderByIdDesc(User user);
}
