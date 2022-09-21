package com.delgo.reward.repository;


import com.delgo.reward.domain.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AchievementsRepository extends JpaRepository<Achievements, Integer>, JpaSpecificationExecutor<Achievements> {

    List<Achievements> findByIsMungple(int isMungple);

    Optional<Achievements> findByAchievementsId(int certificationId);
}

