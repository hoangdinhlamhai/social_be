package com.hdlh.social_be.controller;

import com.hdlh.social_be.domain.dto.skill.*;
import com.hdlh.social_be.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/skills")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public ResponseEntity<List<SkillResponseDTO>> getSkills(@RequestParam String email) {
        return ResponseEntity.ok(skillService.getSkillsByEmail(email));
    }

    @PostMapping
    public ResponseEntity<SkillResponseDTO> createSkill(@Valid @RequestBody SkillRequestDTO request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(skillService.createSkill(request, authentication.getName()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SkillResponseDTO> updateSkill(@PathVariable Long id,
            @Valid @RequestBody SkillRequestDTO request, Authentication authentication) {
        return ResponseEntity.ok(skillService.updateSkill(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(@PathVariable Long id, Authentication authentication) {
        skillService.deleteSkill(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
