package com.delgo.reward.repository;


import com.delgo.reward.domain.achievements.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AchievementsRepository extends JpaRepository<Achievements, Integer>, JpaSpecificationExecutor<Achievements> {

    @Query(value = "select * from achievements where achievements_id  not in (select achievements_id from archive where user_id = ?) and is_mungple = ?", nativeQuery = true)
    List<Achievements> findAchievementsNotEarned(int userId, boolean isMungple);
}

