package com.delgo.reward.repository;

import com.delgo.reward.domain.ranking.RankingPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankingPointRepository extends JpaRepository<RankingPoint, Integer> {
    Optional<RankingPoint> findByUserId(int userId);
}
