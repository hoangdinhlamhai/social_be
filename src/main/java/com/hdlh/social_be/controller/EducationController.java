package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.education.*;
import com.hdlh.social_be.service.EducationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/educations")
public class EducationController {
    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    public ResponseEntity<List<EducationResponseDTO>> getEducations(@RequestParam String email) {
        return ResponseEntity.ok(educationService.handleGetEducationsByEmail(email));
    }

    @PostMapping
    public ResponseEntity<EducationResponseDTO> createEducation(@Valid @RequestBody EducationRequestDTO request,
                                                                Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(educationService.createEducation(request, authentication.getName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EducationResponseDTO> updateEducation(@PathVariable Long id,
            @Valid @RequestBody EducationRequestDTO request, Authentication authentication) {
        return ResponseEntity.ok(educationService.handleUpdateEducation(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id, Authentication authentication) {
        educationService.handleDeleteEducation(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
