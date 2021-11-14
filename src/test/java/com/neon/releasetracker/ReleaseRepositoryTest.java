package com.neon.releasetracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.models.Release;
import com.neon.releasetracker.repositories.ReleaseRepository;

@DataJpaTest
public class ReleaseRepositoryTest {
	
	@Autowired
	private ReleaseRepository underTest;
	
	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}
	
	@Test
	void checkIfReleaseExistsByName() {
		//given
		var releaseName = "Test release 1";
		var release = new Release(
				releaseName, 
				"Test description 1", 
				Status.CREATED, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		
		underTest.save(release);
		//when
		var result = underTest.findReleaseByName(releaseName);
		//then
		assertEquals(releaseName, result.get().getName());
	}
	
	@Test
	void checkIfReleaseDoesNotExistByName() {
		//given
		var releaseName = "Test release 1";
		//when
		var result = underTest.findReleaseByName(releaseName);
		//then
		assertFalse(result.isPresent() && releaseName.equals(result.get().getName()));
	}
	
	@Test
	void checkIfReleaseExistsByStatus() {
		//given
		var releaseOnProd = new Release(
				"release on prod", 
				"release on prod description", 
				Status.ON_PROD, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		
		underTest.save(releaseOnProd);
		
		var releaseCreated = new Release(
				"release created", 
				"release created description ", 
				Status.CREATED, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		underTest.save(releaseCreated);
		
		var releaseOnDev = new Release(
				"release on dev", 
				"release on dev description ", 
				Status.ON_DEV, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		underTest.save(releaseOnDev);
		//when
		var result = underTest.findReleasesByStatus(Status.ON_PROD);
		//then
		assertTrue(result.stream()
				.anyMatch(release -> releaseOnProd.getName().equals(release.getName())));
	}
	
	@Test
	void checkIfReleaseDoesNotExistByStatus() {
		//given
		var releaseOnProd = new Release(
				"release on prod", 
				"release on prod description", 
				Status.ON_PROD, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		
		underTest.save(releaseOnProd);
		
		var releaseCreated = new Release(
				"release created", 
				"release created description ", 
				Status.CREATED, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		underTest.save(releaseCreated);
		
		var releaseOnDev = new Release(
				"release on dev", 
				"release on dev description ", 
				Status.ON_DEV, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		underTest.save(releaseOnDev);
		//when
		var result = underTest.findReleasesByStatus(Status.IN_DEVELOPMENT);
		//then
		assertTrue(result.isEmpty());
	}

}
