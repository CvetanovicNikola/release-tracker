package com.neon.releasetracker.repository;

import com.neon.releasetracker.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long>, JpaSpecificationExecutor<Release> {

    @Query("SELECT r FROM Release r WHERE r.name = :releaseName")
    Optional<Release> findReleaseByName(@Param("releaseName") String releaseName);
}

