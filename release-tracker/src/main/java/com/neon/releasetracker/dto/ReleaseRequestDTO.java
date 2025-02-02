package com.neon.releasetracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neon.releasetracker.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ReleaseRequestDTO (
        @NotBlank(message = "Name cannot be empty")
        @Size(max = 100, message = "Name cannot exceed 100 characters", groups = UpdateDtoGroup.class)
        String name,
        @NotBlank(message = "Description cannot be empty")
        @Size(max = 500, message = "Description cannot exceed 500 characters", groups = UpdateDtoGroup.class)
        String description,
        @Enumerated(EnumType.STRING)
        @NotNull(message = "Status is required")
        Status status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @NotNull(message = "Release date is required")
        @FutureOrPresent(message = "Release date cannot be in the past", groups = UpdateDtoGroup.class)
        LocalDate releaseDate) {
}




