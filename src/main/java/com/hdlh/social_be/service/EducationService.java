package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.Education;
import com.hdlh.social_be.domain.User;
import com.hdlh.social_be.domain.dto.education.*;
import com.hdlh.social_be.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class EducationService {
    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    public EducationService(EducationRepository educationRepository, UserRepository userRepository) {
        this.educationRepository = educationRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<EducationResponseDTO> handleGetEducationsByEmail(String email) {
        return educationRepository.findAllByUserOrderByCurrentDescStartDateDescIdDesc(requireUser(email))
                .stream().map(this::toResponse).toList();
    }

    public EducationResponseDTO createEducation(EducationRequestDTO request, String email) {
        validate(request);
        Education education = new Education();
        education.setUser(requireUser(email));
        apply(education, request);
        return toResponse(educationRepository.save(education));
    }

    public EducationResponseDTO handleUpdateEducation(Long id, EducationRequestDTO request, String email) {
        Education education = requireOwned(id, email);
        validate(request);
        apply(education, request);
        return toResponse(educationRepository.save(education));
    }

    public void handleDeleteEducation(Long id, String email) {
        educationRepository.delete(requireOwned(id, email));
    }

    private User requireUser(String email) {
        User user = userRepository.findByAccount_Email(email);
        if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hồ sơ.");
        return user;
    }

    private Education requireOwned(Long id, String email) {
        User user = requireUser(email);
        Education education = educationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy học vấn."));
        if (education.getUser() == null || !Objects.equals(education.getUser().getId(), user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền thay đổi học vấn này.");
        }
        return education;
    }

    private void validate(EducationRequestDTO request) {
        if (request.getSchoolName() == null || request.getSchoolName().isBlank()
                || tooLong(request.getSchoolName(), 255) || tooLong(request.getDegree(), 255)
                || tooLong(request.getFieldOfStudy(), 255) || tooLong(request.getClassName(), 255)
                || tooLong(request.getDescription(), 5000)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên trường hoặc độ dài nội dung không hợp lệ.");
        }
        var start = request.getStartDate();
        var end = request.getEndDate();
        YearMonth now = YearMonth.now();
        if (start == null || YearMonth.from(start).isAfter(now)
                || (!request.isCurrent() && (end == null || YearMonth.from(end).isAfter(now)
                || YearMonth.from(end).isBefore(YearMonth.from(start))))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian học không hợp lệ.");
        }
    }

    private boolean tooLong(String value, int max) { return value != null && value.length() > max; }
    private String trim(String value) { return value == null ? null : value.trim(); }

    private void apply(Education education, EducationRequestDTO request) {
        education.setSchoolName(request.getSchoolName().trim());
        education.setDegree(trim(request.getDegree()));
        education.setFieldOfStudy(trim(request.getFieldOfStudy()));
        education.setClassName(trim(request.getClassName()));
        education.setDescription(trim(request.getDescription()));
        education.setStartDate(request.getStartDate().withDayOfMonth(1));
        education.setEndDate(request.isCurrent() ? null : request.getEndDate().withDayOfMonth(1));
        education.setCurrent(request.isCurrent());
    }

    private EducationResponseDTO toResponse(Education education) {
        return new EducationResponseDTO(education.getId(), education.getSchoolName(), education.getDegree(),
                education.getFieldOfStudy(), education.getClassName(), education.getStartDate(),
                education.getEndDate(), education.isCurrent(), education.getDescription());
    }
}
