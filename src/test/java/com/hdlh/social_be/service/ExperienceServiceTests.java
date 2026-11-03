package com.hdlh.social_be.service;

import com.hdlh.social_be.domain.*;
import com.hdlh.social_be.domain.dto.experience.ExperienceRequestDTO;
import com.hdlh.social_be.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExperienceServiceTests {
    @Mock ExperienceRepository repository;
    @Mock UserRepository users;
    ExperienceService service;
    User owner;
    ExperienceRequestDTO request;

    @BeforeEach void setup() {
        service = new ExperienceService(repository, users);
        owner = new User(); owner.setId(1L);
        request = new ExperienceRequestDTO();
        request.setTitle(" Developer "); request.setCompanyName(" Company ");
        request.setStartDate(LocalDate.of(2024, 1, 1)); request.setCurrent(true);
    }

    @Test void createsForAuthenticatedUserAndClearsEndDate() {
        when(users.findByAccount_Email("owner")).thenReturn(owner);
        when(repository.save(any())).thenAnswer(inv -> {
            Experience exp = inv.getArgument(0); exp.setId(9L); return exp;
        });
        request.setEndDate(LocalDate.of(2025, 1, 1));
        var result = service.createExperience(request, "owner");
        assertEquals("Developer", result.getTitle());
        assertEquals("Company", result.getCompanyName());
        assertNull(result.getEndDate());
        assertTrue(result.isCurrent());
        verify(repository).save(argThat(exp -> exp.getUser() == owner));
    }

    @Test void listsEmptyProfile() {
        when(users.findByAccount_Email("owner")).thenReturn(owner);
        when(repository.findAllByUserOrderByIsCurrentDescStartDateDescIdDesc(owner)).thenReturn(List.of());
        assertTrue(service.handleGetExperiencesByEmail("owner").isEmpty());
    }

    @Test void mapsLegacyCompanyWithoutReturningEntities() {
        when(users.findByAccount_Email("owner")).thenReturn(owner);
        Experience exp = experience(owner);
        Company company = new Company(); company.setName("Legacy"); exp.setCompany(company);
        when(repository.findAllByUserOrderByIsCurrentDescStartDateDescIdDesc(owner)).thenReturn(List.of(exp));
        assertEquals("Legacy", service.handleGetExperiencesByEmail("owner").get(0).getCompanyName());
    }

    @Test void rejectsForeignOwnerForUpdateAndDelete() {
        User other = new User(); other.setId(2L);
        when(users.findByAccount_Email("owner")).thenReturn(owner);
        when(repository.findById(9L)).thenReturn(Optional.of(experience(other)));
        assertStatus(403, () -> service.handleUpdateExperience(9L, request, "owner"));
        assertStatus(403, () -> service.handleDeleteExperience(9L, "owner"));
        verify(repository, never()).save(any()); verify(repository, never()).delete(any());
    }

    @Test void updatesAndDeletesOwnedRecord() {
        when(users.findByAccount_Email("owner")).thenReturn(owner);
        Experience exp = experience(owner);
        when(repository.findById(9L)).thenReturn(Optional.of(exp));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        assertEquals("Developer", service.handleUpdateExperience(9L, request, "owner").getTitle());
        service.handleDeleteExperience(9L, "owner");
        verify(repository).delete(exp);
    }

    @Test void missingUserOrRecordIsNotFound() {
        assertStatus(404, () -> service.handleGetExperiencesByEmail("missing"));
        when(users.findByAccount_Email("owner")).thenReturn(owner);
        assertStatus(404, () -> service.handleDeleteExperience(9L, "owner"));
    }

    @Test void rejectsInvalidFieldsAndDates() {
        request.setTitle("  "); assertInvalid(); request.setTitle("Developer");
        request.setCompanyName(" "); assertInvalid(); request.setCompanyName("Company");
        request.setStartDate(null); assertInvalid();
        request.setStartDate(LocalDate.now().plusMonths(1)); assertInvalid();
        request.setStartDate(LocalDate.of(2024, 1, 1));
        request.setCurrent(false); assertInvalid();
        request.setEndDate(LocalDate.of(2023, 12, 1)); assertInvalid();
        request.setEndDate(LocalDate.now().plusMonths(1)); assertInvalid();
        request.setEndDate(LocalDate.of(2025, 1, 1));
        request.setDescription("x".repeat(5001)); assertInvalid();
    }

    private void assertInvalid() {
        assertStatus(400, () -> service.createExperience(request, "owner"));
        verify(repository, never()).save(any());
    }
    private Experience experience(User user) {
        Experience exp = new Experience(); exp.setId(9L); exp.setUser(user); return exp;
    }
    private void assertStatus(int status, Runnable action) {
        assertEquals(status, assertThrows(ResponseStatusException.class, action::run).getStatusCode().value());
    }
}
