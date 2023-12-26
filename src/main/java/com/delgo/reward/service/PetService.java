package com.delgo.reward.service;


import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.record.user.ModifyPetRecord;
import com.delgo.reward.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final CodeService codeService;

    @Transactional
    public Pet register(Pet pet) {
        return petRepository.save(pet.setBreedName(codeService.getOneByCode(pet.getBreed()).getCodeName()));
    }

    @Transactional
    public void changePetInfo(ModifyPetRecord modifyPetRecord,User user){
        Optional.ofNullable(modifyPetRecord.name()).ifPresent(user.getPet()::setName);
        Optional.ofNullable(modifyPetRecord.birthday()).ifPresent(user.getPet()::setBirthday);
        Optional.ofNullable(modifyPetRecord.breed()).ifPresent(breed ->{
            user.getPet().setBreed(breed);
            user.getPet().setBreedName(codeService.getOneByCode(breed).getCodeName());
        });
    }

    public void delete(int userId){
        petRepository.deleteByUserUserId(userId);
    }
}