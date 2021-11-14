package com.neon.releasetracker.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.neon.releasetracker.enums.Status;
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
	
	@GetMapping("/releases")
	public List<Release> getAllreleases(
			@RequestParam(required = false) Optional<Status> status,
			@RequestParam(required = false) Optional<String> name
			){
		return releaseService.getAllReleases(status, name);
	}
	
	@GetMapping("/releases/id")
	public Release getRelease(@RequestParam Long id) {
		return releaseService.getRelease(id);
	}
	
	@PostMapping("/releases")
	public Release createNewRelease(@RequestBody Release newRelease) {
		return releaseService.createNewRelease(newRelease);
	}
	
	@PutMapping("/releases")
	public Release updateRelease(@RequestParam Long id, @RequestBody Release updatedRelease) {
		return releaseService.updateRelease(id, updatedRelease);
	}
	
	@DeleteMapping("/releases")
	public void deleteRelease (@RequestParam Long id) {
		releaseService.deleteRelease(id);
	} 

}
