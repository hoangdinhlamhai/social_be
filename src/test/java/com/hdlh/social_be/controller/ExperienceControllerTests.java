package com.hdlh.social_be.controller;

import com.hdlh.social_be.auth.GoogleAuthenticationProvider;
import com.hdlh.social_be.config.SecurityConfiguration;
import com.hdlh.social_be.service.ExperienceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExperienceController.class)
@Import(SecurityConfiguration.class)
class ExperienceControllerTests {
    @Autowired MockMvc mvc;
    @MockitoBean ExperienceService service;
    @MockitoBean UserDetailsService users;
    @MockitoBean GoogleAuthenticationProvider google;
    private final String valid = """
        {"title":"Developer","companyName":"Acme","startDate":"2024-01-01","current":true}
        """;

    @Test void requiresAuthentication() throws Exception {
        mvc.perform(get("/api/experiences").param("email", "owner")).andExpect(status().isUnauthorized());
    }
    @Test void jwtCanCreateUpdateDeleteWithoutCsrfToken() throws Exception {
        mvc.perform(post("/api/experiences").with(jwt().jwt(j -> j.subject("owner")))
                .contentType("application/json").content(valid)).andExpect(status().isCreated());
        verify(service).createExperience(any(), eq("owner"));
        mvc.perform(patch("/api/experiences/1").with(jwt().jwt(j -> j.subject("owner")))
                .contentType("application/json").content(valid)).andExpect(status().isOk());
        mvc.perform(delete("/api/experiences/1").with(jwt().jwt(j -> j.subject("owner"))))
                .andExpect(status().isNoContent());
    }
    @Test void validatesRequestBody() throws Exception {
        mvc.perform(post("/api/experiences").with(jwt()).contentType("application/json")
                .content("{\"title\":\" \"}")).andExpect(status().isBadRequest());
        mvc.perform(post("/api/experiences").with(jwt()).contentType("application/json")
                .content("{\"startDate\":\"not-a-date\"}")).andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }
}
