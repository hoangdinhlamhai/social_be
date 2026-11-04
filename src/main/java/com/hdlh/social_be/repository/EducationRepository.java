package com.hdlh.social_be.repository;

import com.hdlh.social_be.domain.Education;
import com.hdlh.social_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findAllByUserOrderByCurrentDescStartDateDescIdDesc(User user);
}
