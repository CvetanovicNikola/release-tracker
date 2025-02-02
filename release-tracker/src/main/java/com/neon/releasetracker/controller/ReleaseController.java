package com.neon.releasetracker.controller;

import com.neon.releasetracker.dto.ReleaseRequestDTO;
import com.neon.releasetracker.dto.ReleaseResponseDTO;
import com.neon.releasetracker.dto.UpdateDtoGroup;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.service.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/releases")
@Validated
public class ReleaseController {
    private static final Logger logger = LoggerFactory.getLogger((ReleaseController.class));
    private final ReleaseService releaseService;
    @Value("${logging.applogs.enable}")
    private boolean enableAppLogs;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @Operation(summary = "Welcome", description = "Welcome page.")
    @GetMapping("/welcome")
    public String getHome(){
        return "Welcome to Release Tracker Service";
    }

    @Operation(summary = "Get all releases", description = "Fetches all releases.")
    @GetMapping()
    public ResponseEntity<List<ReleaseResponseDTO>> getReleases(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate releaseDate,
            @PageableDefault(size = 5, sort = "releaseDate", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        if(enableAppLogs) {
            logger.info("Req to list all releases");
        }

        var releases = releaseService.getAllReleases(status, name, description, releaseDate, pageable);
        var releasesResponseDto = releases.stream().
                map(releaseService::mapToResponseDTO)
                .toList();
        return ResponseEntity.ok(releasesResponseDto);
    }

    @Operation(summary = "Get single release by Id", description = "Fetches a release by its Id.")
    @GetMapping("/{id}")
    public ResponseEntity<ReleaseResponseDTO> getRelease(@PathVariable Long id) {
        if(enableAppLogs) {
            logger.info("Req to get release with id:" + id);
        }

        var release = releaseService.getReleaseById(id);
        var releaseResponseDto = releaseService.mapToResponseDTO(release);
        return ResponseEntity.ok(releaseResponseDto);
    }

    @Operation(summary = "Create release", description = "Creates a release.")
    @PostMapping()
    public ResponseEntity<ReleaseResponseDTO> createRelease(
            @RequestBody ReleaseRequestDTO releaseReqDto) {
        if(enableAppLogs) {
            logger.info("Req to create release, name:" + releaseReqDto.name()
                    + " description:" + releaseReqDto.description()
                    + " status:" + releaseReqDto.status()
                    + " release date:" + releaseReqDto.releaseDate());
        }

        var newRelease = releaseService.createRelease(releaseReqDto);
        var releaseResponseDto = releaseService.mapToResponseDTO(newRelease);
        return ResponseEntity.status(HttpStatus.CREATED).body(releaseResponseDto);
    }

    @Operation(summary = "Update release", description = "Updates a release.")
    @PutMapping("/{id}")
    public ResponseEntity<ReleaseResponseDTO> updateRelease(
            @PathVariable Long id,
            @Validated(UpdateDtoGroup.class) @RequestBody ReleaseRequestDTO releaseReqDto) {
        if(enableAppLogs) {
            logger.info("Req to update release with id " + id
                    + ", name:" + releaseReqDto.name()
                    + " description:" + releaseReqDto.description()
                    + " status:" + releaseReqDto.status()
                    + " release date:" + releaseReqDto.releaseDate());
        }

        var updatedRelease = releaseService.updateRelease(id, releaseService.mapToEntity(releaseReqDto));
        var releaseResponseDto = releaseService.mapToResponseDTO(updatedRelease);
        return ResponseEntity.ok(releaseResponseDto);
    }

    @Operation(summary = "Delete release", description = "Deletes a release by its Id.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelease(@PathVariable Long id) {
        if(enableAppLogs) {
            logger.info("Req to delete release with id:" + id);
        }

        releaseService.deleteRelease(id);
        return ResponseEntity.noContent().build();
    }
}
