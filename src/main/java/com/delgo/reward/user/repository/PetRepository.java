package com.delgo.reward.user.repository;


import com.delgo.reward.user.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    void deleteByUserUserId(int userId);
}
