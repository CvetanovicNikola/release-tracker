package com.neon.releasetracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.neon.releasetracker.controllers.ReleaseController;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReleaseControllerTest {
	
	@Autowired
	private ReleaseController underTest;
	
	@Test
	void checkIfReleaseConrollerExists() throws Exception {
		assertThat(underTest).isNotNull();
	}

}
