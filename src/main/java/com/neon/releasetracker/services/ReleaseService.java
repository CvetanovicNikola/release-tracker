package com.neon.releasetracker.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.exceptions.MissingReleaseParameterException;
import com.neon.releasetracker.exceptions.ReleaseAlreadyExistsException;
import com.neon.releasetracker.exceptions.ReleaseNotFoundException;
import com.neon.releasetracker.models.Release;
import com.neon.releasetracker.repositories.ReleaseRepository;
import com.neon.releasetracker.utils.ExceptionMessageFormatter;

@Service
public class ReleaseService {
	//String constants to avoid using magic string.
	private static final String NAME = "name";
	private static final String DESCRIPTION = "description";
	private static final String STATUS = "status";
	
	@Autowired
	private final ReleaseRepository releaseRepository;
	
	public ReleaseService(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}

	//get all releases or filter by status or name if the param was provided in the url.
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
	
	//get a single release by id.
	public Release getRelease(Long releaseId) {
		var release = releaseRepository
				.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundById(releaseId)));
		return release;
	}
	
	//Extracted a piece of functionality since it is used on a couple of places.
	private Optional<Release> findReleaseByName (String releaseName) {
		return releaseRepository.findReleaseByName(releaseName);
	}
	
	//Create a new release if it doesn`t already exist and if all params are present.
	public Release createNewRelease(Release release) {
		if(findReleaseByName(release.getName()).isPresent()) 
			throw new ReleaseAlreadyExistsException(ExceptionMessageFormatter.releaseAlreadyExists(release.getName()));
		
		checkRequiredReleaseParams(release.getName(), release.getDescription(), release.getStatus());
			
		var newRelease = releaseRepository.save(new Release(
				release.getName(),
				release.getDescription(),
				release.getStatus(),
				release.getReleaseDate(),
				LocalDateTime.now(),
				LocalDateTime.now()));
		
		return newRelease;
	}
	
	//update release if it exists and if the required params are present.
	public Release updateRelease(Long releaseId, Release updatedRelease) {
		var releaseToUpdate = releaseRepository
				.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundById(releaseId)));
		
		checkRequiredReleaseParams(updatedRelease.getName(), updatedRelease.getDescription(), updatedRelease.getStatus());
		
		releaseToUpdate.setName(updatedRelease.getName());
		releaseToUpdate.setDescription(updatedRelease.getDescription());
		releaseToUpdate.setStatus(updatedRelease.getStatus());
		releaseToUpdate.setReleaseDate(updatedRelease.getReleaseDate());
		releaseToUpdate.setLastUpdateAt(LocalDateTime.now());
		
		releaseRepository.save(releaseToUpdate);
		
		return releaseToUpdate;
	}
	
	//delete a release by id
	public void deleteRelease(Long releaseId) {
		var releaseToDelete = releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ReleaseNotFoundException(ExceptionMessageFormatter.releaseNotFoundById(releaseId)));

		releaseRepository.delete(releaseToDelete);
	}
	
	//checking if all required params are present.
	private void checkRequiredReleaseParams(String name, String description, Status status) {
		if(Objects.isNull(name) || name.isEmpty()) throw new MissingReleaseParameterException(ExceptionMessageFormatter.missingReleaseParameter(NAME));
		if(Objects.isNull(description) || description.isEmpty()) throw new MissingReleaseParameterException(ExceptionMessageFormatter.missingReleaseParameter(DESCRIPTION));
		if(Objects.isNull(status))	throw new MissingReleaseParameterException(ExceptionMessageFormatter.missingReleaseParameter(STATUS));
	}
	
}

