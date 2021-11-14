package com.neon.releasetracker.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.exceptions.ReleaseAlreadyExistsException;
import com.neon.releasetracker.exceptions.ReleaseNotFoundException;
import com.neon.releasetracker.models.Release;
import com.neon.releasetracker.repositories.ReleaseRepository;
import com.neon.releasetracker.utils.ExceptionMessageFormatter;

@Service
public class ReleaseService {
	
	@Autowired
	private final ReleaseRepository releaseRepository;
	
	public ReleaseService(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}

	public List<Release> getAllReleases(Optional<Status> status, Optional<String> name) {
		List<Release> releases = new ArrayList<Release>();
		if(status.isPresent()) {
			var releasesByStatus = releaseRepository.findReleasesByStatus(status.get());
			if(releasesByStatus.isEmpty()) 
				throw new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundByStatus(status.get().name()));
			releases = releasesByStatus;
		} else if(name.isPresent()){
			releases.add(findReleaseByName(name.get())
					.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundByName(name.get()))));
		} else {
			releases = releaseRepository.findAll();
		}
		return releases;
	}
	
	public Release getRelease(Long releaseId) {
		var release = releaseRepository
				.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundById(releaseId)));
		return release;
	}
	
	private Optional<Release> findReleaseByName (String releaseName) {
		return releaseRepository.findReleaseByName(releaseName);
	}
	
	public Release getReleaseByName(String releaseName) {
		var release = findReleaseByName(releaseName)
				.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundByName(releaseName)));
		return release;
	}
	
	public Release createNewRelease(Release release) {
		if(findReleaseByName(release.getName()).isPresent()) 
			throw new ReleaseAlreadyExistsException(ExceptionMessageFormatter.releaseAlreadyExists(release.getName()));
		
		var newRelease = releaseRepository.save(new Release(
				release.getName(),
				release.getDescription(),
				release.getStatus(),
				release.getReleaseDate(),
				LocalDateTime.now(),
				LocalDateTime.now()));
		
		return newRelease;
	}
	
	public Release updateRelease(Long releaseId, Release updatedRelease) {
		var releaseToUpdate = releaseRepository
				.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundById(releaseId)));
		
		releaseToUpdate.setName(updatedRelease.getName());
		releaseToUpdate.setDescription(updatedRelease.getDescription());
		releaseToUpdate.setStatus(updatedRelease.getStatus());
		releaseToUpdate.setReleaseDate(updatedRelease.getReleaseDate());
		releaseToUpdate.setLastUpdateAt(LocalDateTime.now());
		
		releaseRepository.save(releaseToUpdate);
		
		return releaseToUpdate;
	}
	
	public void deleteRelease(Long releaseId) {
		var releaseToDelete = releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundById(releaseId)));

		releaseRepository.delete(releaseToDelete);
	}
	
}

