package com.neon.releasetracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.models.Release;
import com.neon.releasetracker.repositories.ReleaseRepository;
import com.neon.releasetracker.services.ReleaseService;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ReleaseServiceTest {
	
	@Mock
	private ReleaseRepository releaseRepository;
	private ReleaseService underTest;
	
	@BeforeEach
	void setUp() {
		underTest = new ReleaseService(releaseRepository);
	}
	
	@Test
	void canGetAllReleases() {
		//when
		underTest.getAllReleases(Optional.ofNullable(null), Optional.ofNullable(null));
		//then
		verify(releaseRepository).findAll();
	}
	
	@Test
	@Disabled
	void canCreateNewRelease() {
		//given
		var release = new Release(
				"Test name", 
				"Test description", 
				Status.CREATED, 
				LocalDate.now(), 
				LocalDateTime.now(),
				LocalDateTime.now());
		//when
		underTest.createNewRelease(release);
		//then
		ArgumentCaptor<Release> releaseArgumentCaptuator = ArgumentCaptor.forClass(Release.class);
		
		verify(releaseRepository).save(releaseArgumentCaptuator.capture());
		
		var capturedRelease = releaseArgumentCaptuator.getValue();
		
		assertThat(capturedRelease).isEqualTo(release);
	}
	
}
