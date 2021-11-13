package com.neon.releasetracker.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neon.releasetracker.exceptions.ReleaseAlreadyExistsException;
import com.neon.releasetracker.exceptions.ReleaseNotFoundException;
import com.neon.releasetracker.models.Release;
import com.neon.releasetracker.repositories.ReleaseRepository;

@Service
public class ReleaseService {
	
	@Autowired
	private final ReleaseRepository releaseRepository;
	
	public ReleaseService(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}

	public List<Release> getAllReleases() {
		return releaseRepository.findAll();
	}
	
	public Release getRelease(Long releaseId) {
		var release = releaseRepository
				.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(String.format("There is no release with id %s", releaseId)));
		return release;
	}
	
	private Optional<Release> findReleaseByName (String releaseName) {
		return releaseRepository.findReleaseByName(releaseName);
	}
	
	public Release getReleaseByName(String releaseName) {
		var release = findReleaseByName(releaseName)
				.orElseThrow(() -> new ReleaseNotFoundException(String.format("There is no release with name %s", releaseName)));
		return release;
	}
	
	public Release createNewRelease(Release release) {
		if(findReleaseByName(release.getName()).isPresent()) 
			throw new ReleaseAlreadyExistsException(String.format("There is no release with name %s", release.getName()));
		
		var newRelease = releaseRepository.save(new Release(
				release.getName(),
				release.getDescription(),
				//TODO Check if status is correct
				release.getStatus(),
				release.getReleaseDate(),
				release.getCreatedAt(),
				release.getLastUpdateAt()));
		
		return newRelease;
		
		
	}
	
	public Release updateRelease(Long releaseId, Release updatedRelease) {
		var releaseToUpdate = releaseRepository
				.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(String.format("There is no release with id %s", releaseId)));
		
		releaseToUpdate.setName(updatedRelease.getName());
		releaseToUpdate.setDescription(updatedRelease.getDescription());
		//TODO Check if status is correct
		releaseToUpdate.setStatus(updatedRelease.getStatus());
		releaseToUpdate.setReleaseDate(updatedRelease.getReleaseDate());
		releaseToUpdate.setLastUpdateAt(LocalDateTime.now());
		
		releaseRepository.save(releaseToUpdate);
		
		return releaseToUpdate;
	}
	
	public Map<String, Boolean> deleteRelease(Long releaseId) {
		var releaseToDelete = releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(String.format("There is no release with id %s", releaseId)));
		var releaseToDeleteName = releaseToDelete.getName();
		releaseRepository.delete(releaseToDelete);
		var response = new HashMap<String, Boolean>();
		response.put(String.format("Deleted release %s", releaseToDeleteName), true);
		
		return response;
	
	}
	
}

