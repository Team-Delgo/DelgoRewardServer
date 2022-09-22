package com.delgo.reward.repository;

import com.delgo.reward.domain.ranking.RankingPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingPointRepository extends JpaRepository<RankingPoint, Integer> {
    int findByUserId(int userId);
}
