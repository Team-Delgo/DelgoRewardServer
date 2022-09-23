package com.delgo.reward.repository;

import com.delgo.reward.domain.ranking.RankingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingCategoryRepository extends JpaRepository<RankingCategory, Integer> {
    int findByUserIdAndCategoryCode(int userId, String categoryCode);
}
