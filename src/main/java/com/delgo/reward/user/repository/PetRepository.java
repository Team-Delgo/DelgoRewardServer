package com.delgo.reward.user.repository;


import com.delgo.reward.user.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    void deleteByUserUserId(int userId);
    Optional<Pet> findByUserUserId(int userId);
}
