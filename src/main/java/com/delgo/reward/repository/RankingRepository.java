package com.delgo.reward.repository;

import com.delgo.reward.domain.Ranking;
import com.delgo.reward.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Integer> {
    int findByUserIdAndCategoryCode(int userId, String categoryCode);
}
