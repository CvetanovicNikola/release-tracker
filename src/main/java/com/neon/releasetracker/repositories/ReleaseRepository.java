package com.neon.releasetracker.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.neon.releasetracker.enums.Status;
import com.neon.releasetracker.models.Release;



public interface ReleaseRepository extends JpaRepository<Release, Long>{

	@Query("SELECT r FROM Release r WHERE r.name = :releaseName")
	Optional<Release> findReleaseByName(@Param("releaseName") String releaseName);
	
	@Query("SELECT r FROM Release r WHERE r.status = :status")
	List<Release> findReleasesByStatus(@Param("status") Status status);
	
	
}
