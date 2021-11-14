package com.neon.releasetracker.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.neon.releasetracker.enums.Status;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "releases")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Release {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private @Setter(value = AccessLevel.NONE) Long id;
	@Column(name = "name", unique = true, nullable = false, columnDefinition = "TEXT")
	private String name;
	@Column(name = "description", nullable = false, columnDefinition = "TEXT")
	private String description;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, columnDefinition = "TEXT")
	private Status status;
	@Column(name = "date", nullable = false, columnDefinition = "DATE")
	private LocalDate releaseDate;
	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;
	@Column(name = "last_update_at", nullable = false, columnDefinition = "TIMESTAMP")
	private LocalDateTime lastUpdateAt;
	
	public Release(String name, String description, Status status, LocalDate releaseDate, LocalDateTime createdAt,
			LocalDateTime lastUpdateAt) {
				this.name = name;
		this.description = description;
		this.status = status;
		this.releaseDate = releaseDate;
		this.createdAt = createdAt;
		this.lastUpdateAt = lastUpdateAt;
	}
	
	
	
	
}
