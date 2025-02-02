package com.neon.releasetracker.service;

import com.neon.releasetracker.dto.ReleaseRequestDTO;
import com.neon.releasetracker.dto.ReleaseResponseDTO;
import com.neon.releasetracker.entity.Release;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.repository.ReleaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ReleaseServiceTest {
    @Mock
    private ReleaseRepository releaseRepository;

    @InjectMocks
    private ReleaseService releaseService;

    private Release release;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        release = new Release(1L, "Release 1", "First release", Status.CREATED, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testGetReleaseById() {
        when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

        var foundRelease = releaseService.getReleaseById(1L);

        assertNotNull(foundRelease);
        assertEquals("Release 1", foundRelease.getName());
        verify(releaseRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateRelease() {
        when(releaseRepository.save(any(Release.class))).thenReturn(release);
        var releaseDto = new ReleaseRequestDTO(release.getName(), release.getDescription(), release.getStatus(), release.getReleaseDate());
        var created = releaseService.createRelease(releaseDto);

        assertNotNull(created);
        assertEquals("Release 1", created.getName());
        verify(releaseRepository, times(1)).save(any(Release.class));
    }

    @Test
    void testDeleteRelease() {
        var releaseDto = new ReleaseRequestDTO("Release 1", "Description", Status.CREATED, LocalDate.now());
        when(releaseRepository.save(any(Release.class))).thenAnswer(invocation -> {
            Release release = invocation.getArgument(0);
            release.setId(1L);
            return release;
        });

        var created = releaseService.createRelease(releaseDto);
        when(releaseRepository.findById(1L)).thenReturn(Optional.of(created));
        releaseService.deleteRelease(created.getId());
        verify(releaseRepository, times(1)).delete(created);
    }

    @Test
    void testUpdateRelease() {
        Release existingRelease = new Release(1L, "Release 1", "Description", Status.CREATED, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        Release releaseToUpdate = new Release(1L, "Updated Release", "Updated Description", Status.DONE, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());

        when(releaseRepository.findById(1L)).thenReturn(Optional.of(existingRelease));
        when(releaseRepository.save(any(Release.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var updatedRelease = releaseService.updateRelease(1L, releaseToUpdate);
        assertNotNull(updatedRelease);
        assertEquals("Updated Release", updatedRelease.getName());
        assertEquals("Updated Description", updatedRelease.getDescription());
        assertEquals(Status.DONE, updatedRelease.getStatus());

        verify(releaseRepository, times(1)).save(existingRelease);
        verify(releaseRepository, times(1)).findById(1L);
    }
}
