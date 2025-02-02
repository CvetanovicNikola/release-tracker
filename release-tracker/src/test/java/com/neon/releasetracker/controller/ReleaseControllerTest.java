package com.neon.releasetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neon.releasetracker.dto.ReleaseRequestDTO;
import com.neon.releasetracker.dto.ReleaseResponseDTO;
import com.neon.releasetracker.entity.Release;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.service.ReleaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@WebMvcTest(ReleaseController.class)
@ExtendWith(MockitoExtension.class)
public class ReleaseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReleaseService releaseService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Release release = new Release(1L, "Release 1", "First release", Status.CREATED, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
    private final ReleaseResponseDTO releaseResp = new ReleaseResponseDTO(1L, "Release 1", "First release", Status.CREATED, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
    private final ReleaseRequestDTO releaseReq =new ReleaseRequestDTO( "Release 1", "First release", Status.CREATED, LocalDate.now());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(releaseService.createRelease(any(ReleaseRequestDTO.class))).thenReturn(release);
        when(releaseService.mapToResponseDTO(any(Release.class))).thenReturn(releaseResp);
    }

    @Test
    void testGetReleaseById() throws Exception {
        when(releaseService.getReleaseById(1L)).thenReturn(release);

        mockMvc.perform(get("/releases/1")
                        .with(httpBasic("admin", "admin"))
                        .header("Referer", "http://localhost:8081")
                        .header("Origin", "http://localhost:8081")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Release 1"));

        verify(releaseService, times(1)).getReleaseById(1L);

    }

    @Test
    void testDeleteRelease() throws Exception {
        release = releaseService.createRelease(releaseReq);

        mockMvc.perform(delete("/releases/" + release.getId())
                        .with(httpBasic("admin", "admin"))
                        .header("Referer", "http://localhost:8081")
                        .header("Origin", "http://localhost:8081"))
                .andExpect(status().isNoContent());

        verify(releaseService, times(1)).deleteRelease(eq(1L));
    }

    @Test
    void testCreateRelease() throws Exception {
        mockMvc.perform(post("/releases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Release 1\", \"description\": \"First release\", \"status\": \"CREATED\", \"releaseDate\": \"2025-01-01\" }")
                        .with(httpBasic("admin", "admin"))
                        .header("Referer", "http://localhost:8081")
                        .header("Origin", "http://localhost:8081"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Release 1"))
                .andExpect(jsonPath("$.description").value("First release"));

        verify(releaseService, times(1)).createRelease(any(ReleaseRequestDTO.class));
        verify(releaseService, times(1)).mapToResponseDTO(any(Release.class));
    }

    @Test
    void testUpdateRelease() throws Exception {
        ReleaseRequestDTO releaseReqDto = new ReleaseRequestDTO("Updated Release", "Updated Description", Status.CREATED, LocalDate.now());
        Release updatedRelease = new Release(1L, "Updated Release", "Updated Description", Status.DONE, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        ReleaseResponseDTO releaseRespDto = new ReleaseResponseDTO(1L, "Updated Release", "Updated Description", Status.DONE, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());

        when(releaseService.mapToEntity(releaseReqDto)).thenReturn(updatedRelease);
        when(releaseService.updateRelease(1L, updatedRelease)).thenReturn(updatedRelease);
        when(releaseService.mapToResponseDTO(updatedRelease)).thenReturn(releaseRespDto);

        mockMvc.perform(put("/releases/1")
                        .with(httpBasic("admin", "admin"))
                        .header("Referer", "http://localhost:8081")
                        .header("Origin", "http://localhost:8081")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(releaseReqDto))) // Convert DTO to JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Release"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("DONE"));

        verify(releaseService, times(1)).mapToEntity(releaseReqDto);
        verify(releaseService, times(1)).updateRelease(1L, updatedRelease);
        verify(releaseService, times(1)).mapToResponseDTO(updatedRelease);
    }
}
