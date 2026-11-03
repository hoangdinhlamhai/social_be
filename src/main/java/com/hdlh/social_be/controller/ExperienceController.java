package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.experience.*;
import com.hdlh.social_be.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {
    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public ResponseEntity<List<ExperienceResponseDTO>> getExperiences(@RequestParam String email) {
        return ResponseEntity.ok(experienceService.handleGetExperiencesByEmail(email));
    }

    @PostMapping
    public ResponseEntity<ExperienceResponseDTO> createExperience(@Valid @RequestBody ExperienceRequestDTO request,
                                                                  Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(experienceService.createExperience(request, authentication.getName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ExperienceResponseDTO> updateExperience(@PathVariable Long id,
            @Valid @RequestBody ExperienceRequestDTO request, Authentication authentication) {
        return ResponseEntity.ok(experienceService.handleUpdateExperience(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id, Authentication authentication) {
        experienceService.handleDeleteExperience(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
