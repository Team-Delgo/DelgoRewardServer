package com.delgo.reward.common.repository;


import com.delgo.reward.common.domain.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface VersionRepository extends JpaRepository<Version, Integer>, JpaSpecificationExecutor<Version> {
    Optional<Version> findByIsActive(boolean isActive);
}