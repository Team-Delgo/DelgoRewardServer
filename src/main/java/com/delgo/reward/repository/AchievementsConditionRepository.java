package com.delgo.reward.repository;


import com.delgo.reward.domain.achievements.AchievementsCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AchievementsConditionRepository extends JpaRepository<AchievementsCondition, Integer>, JpaSpecificationExecutor<AchievementsCondition> {
}

