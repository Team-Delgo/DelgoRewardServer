package com.delgo.reward.service;


import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.record.user.PetUpdate;
import com.delgo.reward.repository.PetRepository;
import com.delgo.reward.service.user.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final CodeService codeService;
    private final UserQueryService userQueryService;

    @Transactional
    public Pet create(Pet pet) {
        Code code = codeService.getOneByCode(pet.getBreed());
        pet.setBreedName(code.getCodeName());
        Pet savedPet = petRepository.save(pet);

        User user = userQueryService.getOneByUserId(pet.getUserId());
        user.setPet(savedPet);

        return savedPet;
    }

    @Transactional
    public void changePetInfo(PetUpdate petUpdate, User user){
        Optional.ofNullable(petUpdate.name()).ifPresent(user.getPet()::setName);
        Optional.ofNullable(petUpdate.birthday()).ifPresent(user.getPet()::setBirthday);
        Optional.ofNullable(petUpdate.breed()).ifPresent(breed ->{
            user.getPet().setBreed(breed);
            user.getPet().setBreedName(codeService.getOneByCode(breed).getCodeName());
        });
    }

    @Transactional
    public void deleteByUserId(int userId){
        petRepository.deleteByUserUserId(userId);
    }
}