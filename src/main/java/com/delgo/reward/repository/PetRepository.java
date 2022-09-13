package com.delgo.reward.repository;


import com.delgo.reward.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findByUserId(int userId);
}
