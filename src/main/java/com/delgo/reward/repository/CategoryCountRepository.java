package com.delgo.reward.repository;

import com.delgo.reward.domain.user.CategoryCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryCountRepository extends JpaRepository<CategoryCount, Integer> {
    Optional<CategoryCount> findByUserId(int userId);
}
