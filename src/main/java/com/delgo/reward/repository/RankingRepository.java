package com.delgo.reward.repository;

import com.delgo.reward.domain.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Integer> {
}
