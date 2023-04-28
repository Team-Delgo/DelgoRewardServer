package com.delgo.reward.repository;


import com.delgo.reward.domain.Version;
import com.delgo.reward.domain.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


public interface VersionRepository extends JpaRepository<Version, Integer>, JpaSpecificationExecutor<Survey> {
    Optional<Version> findByIsActive(boolean isActive);
}