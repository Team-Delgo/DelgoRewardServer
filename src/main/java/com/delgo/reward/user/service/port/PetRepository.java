package com.delgo.reward.user.service.port;

import com.delgo.reward.user.domain.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepository {
    Optional<Pet> findByUserUserId(int userId);
    Pet save(Pet pet);
    void delete(Pet pet);
    List<Pet> findAll();
}
