package com.neon.releasetracker.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neon.releasetracker.models.Release;
import com.neon.releasetracker.services.ReleaseService;


@RestController
@RequestMapping("/api/v1/")
public class ReleaseController {
	
	private final ReleaseService releaseService;
	
	@Autowired
	public ReleaseController(ReleaseService releaseService) {
		this.releaseService = releaseService;
	}
	
	@GetMapping("/home")
	public String home(){
		return "<H1>Home Page</H1>";
	}
	
	@GetMapping("/releases")
	public List<Release> getAllreleases(){
		return releaseService.getAllReleases();
	}
	
	@GetMapping("/release_id/{releaseId}")
	public Release getRelease(@PathVariable(required = true) Long releaseId) {
		return releaseService.getRelease(releaseId);
	}
	
	@GetMapping("/release_name/{releaseName}")
	public Release getReleaseByName(@PathVariable(required = true) String releaseName) {
		return releaseService.getReleaseByName(releaseName);
	}
	
	@PostMapping("/release_new")
	public Release createNewRelease(@RequestBody Release newRelease) {
		return releaseService.createNewRelease(newRelease);
	}
	
	@PutMapping("/release_update/{releaseId}")
	public Release updateRelease(@PathVariable(required = true) Long releaseId, @RequestBody Release updatedRelease) {
		return releaseService.updateRelease(releaseId, updatedRelease);
	}
	
	@DeleteMapping("/release_delete/{releaseId}")
	public Map<String, Boolean> deleteRelease (@PathVariable(required = true) Long releaseId) {
		return releaseService.deleteRelease(releaseId);
	} 

}
