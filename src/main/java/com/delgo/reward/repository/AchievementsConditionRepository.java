package com.delgo.reward.repository;


import com.delgo.reward.domain.AchievementsCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AchievementsConditionRepository extends JpaRepository<AchievementsCondition, Integer>, JpaSpecificationExecutor<AchievementsCondition> {
    List<AchievementsCondition> findByAchievementsId(int achievementsId);
}

