package com.neon.releasetracker.dto;

import com.neon.releasetracker.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReleaseResponseDTO(
        Long id,
        String name,
        String description,
        Status status,
        LocalDate releaseDate,
        LocalDateTime createdAt,
        LocalDateTime lastUpdateAt) {
}
