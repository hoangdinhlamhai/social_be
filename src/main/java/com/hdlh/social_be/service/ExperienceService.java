package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Experience;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.experience.*;
import com.hdlh.social_be.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;

    public ExperienceService(ExperienceRepository experienceRepository, UserRepository userRepository) {
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponseDTO> handleGetExperiencesByEmail(String email) {
        return experienceRepository.findAllByUserOrderByIsCurrentDescStartDateDescIdDesc(requireUser(email))
                .stream().map(this::toResponse).toList();
    }

    public ExperienceResponseDTO createExperience(ExperienceRequestDTO request, String email) {
        validate(request);
        Experience experience = new Experience();
        experience.setUser(requireUser(email));
        apply(experience, request);
        return toResponse(experienceRepository.save(experience));
    }

    public ExperienceResponseDTO handleUpdateExperience(Long id, ExperienceRequestDTO request, String email) {
        Experience experience = requireOwned(id, email);
        validate(request);
        apply(experience, request);
        return toResponse(experienceRepository.save(experience));
    }

    public void handleDeleteExperience(Long id, String email) {
        experienceRepository.delete(requireOwned(id, email));
    }

    private User requireUser(String email) {
        User user = userRepository.findByAccount_Email(email);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ.");
        return user;
    }

    private Experience requireOwned(Long id, String email) {
        User user = requireUser(email);
        Experience experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy kinh nghiệm."));
        if (experience.getUser() == null || !Objects.equals(experience.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thay đổi kinh nghiệm này.");
        }
        return experience;
    }

    private void validate(ExperienceRequestDTO request) {
        if (request.getTitle() == null || request.getTitle().isBlank()
                || request.getCompanyName() == null || request.getCompanyName().isBlank()
                || tooLong(request.getTitle(), 255) || tooLong(request.getCompanyName(), 255)
                || tooLong(request.getEmploymentType(), 255) || tooLong(request.getLocation(), 255)
                || tooLong(request.getDescription(), 5000)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Chức danh, công ty hoặc độ dài nội dung không hợp lệ.");
        }
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();
        YearMonth now = YearMonth.now();
        if (start == null || YearMonth.from(start).isAfter(now)
                || (!request.isCurrent() && (end == null || YearMonth.from(end).isAfter(now)
                || YearMonth.from(end).isBefore(YearMonth.from(start))))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian làm việc không hợp lệ.");
        }
    }

    private boolean tooLong(String value, int max) {
        return value != null && value.length() > max;
    }

    private void apply(Experience experience, ExperienceRequestDTO request) {
        experience.setTitle(request.getTitle().trim());
        experience.setCompanyName(request.getCompanyName().trim());
        experience.setEmploymentType(trim(request.getEmploymentType()));
        experience.setLocation(trim(request.getLocation()));
        experience.setDescription(trim(request.getDescription()));
        experience.setStartDate(request.getStartDate().withDayOfMonth(1));
        experience.setEndDate(request.isCurrent() ? null : request.getEndDate().withDayOfMonth(1));
        experience.setCurrent(request.isCurrent());
    }

    private String trim(String value) { return value == null ? null : value.trim(); }

    private ExperienceResponseDTO toResponse(Experience experience) {
        String companyName = experience.getCompanyName();
        if (companyName == null && experience.getCompany() != null) companyName = experience.getCompany().getName();
        return new ExperienceResponseDTO(experience.getId(), experience.getTitle(), companyName,
                experience.getEmploymentType(), experience.getLocation(), experience.getStartDate(),
                experience.getEndDate(), experience.isCurrent(), experience.getDescription());
    }
}
