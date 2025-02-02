package com.neon.releasetracker.service;

import com.neon.releasetracker.dto.ReleaseRequestDTO;
import com.neon.releasetracker.dto.ReleaseResponseDTO;
import com.neon.releasetracker.entity.Release;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.exception.InvalidReleaseException;
import com.neon.releasetracker.exception.ReleaseAlreadyExistsException;
import com.neon.releasetracker.exception.ReleaseNotFoundException;
import com.neon.releasetracker.repository.ReleaseRepository;
import com.neon.releasetracker.repository.ReleaseSpecification;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ReleaseService {
    private static final Logger logger = LoggerFactory.getLogger((ReleaseService.class));
    @Value("${logging.applogs.enable}")
    private boolean enableAppLogs;
    private final ReleaseRepository releaseRepository;

    public ReleaseService(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    @Cacheable(value = "releases", key = """
    #status?.name() + '-' + 
    (#name ?: '') + '-' + 
    (#description ?: '') + '-' + 
    (#releaseDate?.toString() ?: '') + '-' + 
    'page-' + #pageable.pageNumber + 
    '-size-' + #pageable.pageSize + 
    '-sort-' + #pageable.sort.toString()
""")
    public Page<Release> getAllReleases(
            Status status,
            String name,
            String description,
            LocalDate releaseDate,
            Pageable pageable) {
        Specification<Release> specification = ReleaseSpecification.filterByParam(status, name, description, releaseDate);
        if(enableAppLogs)
            logger.info("Finding all releases");

        return releaseRepository.findAll(specification, pageable);
    }

    @Cacheable(value = "release", key = "#id")
    public Release getReleaseById(Long id) {
        if(enableAppLogs)
            logger.info("Finding release with id:" + id);

        return releaseRepository.findById(id)
                .orElseThrow(() -> new ReleaseNotFoundException("Release not found with id:" + id));
    }

    private Optional<Release> findReleaseByName (String releaseName) {
        return releaseRepository.findReleaseByName(releaseName);
    }

    @CacheEvict(value = "releases", allEntries = true)
    public Release createRelease(ReleaseRequestDTO releaseReqDto) {
        if(enableAppLogs)
            logger.info("Creating release");

        var release = mapToEntity(releaseReqDto);

        if(findReleaseByName(release.getName()).isPresent())
            throw new ReleaseAlreadyExistsException("Release already exists with the name:" + release.getName());


        return releaseRepository.save(release);
    }

    @CacheEvict(value = "release", key = "#id")
    @Transactional
    public Release updateRelease(Long id, Release updateRelease) {
        if(enableAppLogs)
            logger.info("Updating release with id:" + id);

        var existingRelease = getReleaseById(id);
        if(updateRelease.getName() != null) existingRelease.setName(updateRelease.getName());
        if(updateRelease.getDescription() != null) existingRelease.setDescription(updateRelease.getDescription());
        if(updateRelease.getStatus() != null) existingRelease.setStatus(updateRelease.getStatus());
        if(updateRelease.getReleaseDate() != null) existingRelease.setReleaseDate(updateRelease.getReleaseDate());
        return  releaseRepository.save(existingRelease);
    }

    @CacheEvict(value = {"release", "releases"}, key = "#id", allEntries = true)
    public void deleteRelease(Long id) {
        if(enableAppLogs)
            logger.info("Deleting release with id:" + id);
        var release = getReleaseById(id);
        releaseRepository.delete(release);
    }

    public Release mapToEntity(ReleaseRequestDTO dto) {
        var release = new Release();
        release.setName(dto.name());
        release.setDescription(dto.description());
        release.setReleaseDate(dto.releaseDate());
        release.setStatus(dto.status());
        return release;
    }

    public ReleaseResponseDTO mapToResponseDTO(Release release) {
        return new ReleaseResponseDTO(
                release.getId(),
                release.getName(),
                release.getDescription(),
                release.getStatus(),
                release.getReleaseDate(),
                release.getCreatedAt(),
                release.getLastUpdatedAt());
    }
}
