package com.delgo.reward.user.infrastructure.jpa;


import com.delgo.reward.user.infrastructure.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetJpaRepository extends JpaRepository<PetEntity, Integer> {
    Optional<PetEntity> findByUserUserId(int userId);
}
