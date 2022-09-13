package com.delgo.reward.service;


import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    @Transactional
    public void changePetInfo(Pet pet){
        petRepository.save(pet);
    }

    public Pet getPetByUserId(int userId) {
        return petRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalStateException());
    }

}
