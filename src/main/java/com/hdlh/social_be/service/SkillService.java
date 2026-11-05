package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Skill;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.skill.*;
import com.hdlh.social_be.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class SkillService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public SkillService(SkillRepository skillRepository, UserRepository userRepository) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<SkillResponseDTO> getSkillsByEmail(String email) {
        return skillRepository.findAllByUserOrderByIdDesc(requireUser(email))
                .stream().map(this::toResponse).toList();
    }

    public SkillResponseDTO createSkill(SkillRequestDTO request, String email) {
        Skill skill = new Skill();
        skill.setUser(requireUser(email));
        apply(skill, request);
        return toResponse(skillRepository.save(skill));
    }

    public SkillResponseDTO updateSkill(Long id, SkillRequestDTO request, String email) {
        Skill skill = requireOwned(id, email);
        apply(skill, request);
        return toResponse(skillRepository.save(skill));
    }

    public void deleteSkill(Long id, String email) {
        skillRepository.delete(requireOwned(id, email));
    }

    private User requireUser(String email) {
        User user = userRepository.findByAccount_Email(email);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ.");
        return user;
    }

    private Skill requireOwned(Long id, String email) {
        User user = requireUser(email);
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy kỹ năng."));
        if (skill.getUser() == null || !Objects.equals(skill.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thay đổi kỹ năng này.");
        }
        return skill;
    }

    private void apply(Skill skill, SkillRequestDTO request) {
        skill.setSkillName(request.getSkillName().trim());
        skill.setSkillDescription(request.getSkillDescription() == null ? null : request.getSkillDescription().trim());
    }

    private SkillResponseDTO toResponse(Skill skill) {
        return new SkillResponseDTO(skill.getId(), skill.getSkillName(), skill.getSkillDescription());
    }
}
