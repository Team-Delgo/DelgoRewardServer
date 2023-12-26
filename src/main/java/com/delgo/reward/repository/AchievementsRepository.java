package com.delgo.reward.repository;

import com.delgo.reward.domain.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AchievementsRepository extends JpaRepository<Achievements, Integer>, JpaSpecificationExecutor<Achievements> {
}


