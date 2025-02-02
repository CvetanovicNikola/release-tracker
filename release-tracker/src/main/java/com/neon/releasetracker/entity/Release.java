package com.neon.releasetracker.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.neon.releasetracker.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "VARCHAR")
    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "VARCHAR")
    @NotBlank(message = "Description cannot be empty")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private Status status;

    @Column(name = "release_date", nullable = false, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "Release date is required")
    @FutureOrPresent(message = "Release date cannot be in the past")
    private LocalDate releaseDate;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_update_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime lastUpdatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        lastUpdatedAt = createdAt;
    }
    @PreUpdate
    public void onUpdate() {
        lastUpdatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public Release(Long id,
                   String name,
                   String description,
                   Status status,
                   LocalDate releaseDate,
                   LocalDateTime createdAt,
                   LocalDateTime lastUpdatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Release(String name,
                   String description,
                   Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Release() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }
}
