package com.delgo.reward.repository;


import com.delgo.reward.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    void deleteByUserUserId(int userId);
}
