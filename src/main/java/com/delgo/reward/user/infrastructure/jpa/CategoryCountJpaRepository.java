package com.delgo.reward.user.infrastructure.jpa;

import com.delgo.reward.user.infrastructure.entity.CategoryCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryCountJpaRepository extends JpaRepository<CategoryCountEntity, Integer> {
    Optional<CategoryCountEntity> findByUserId(int userId);
    void deleteByUserId(int userId);
}
