package com.delgo.reward.service;


import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.record.user.ModifyPetRecord;
import com.delgo.reward.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PetService {

    // Service
    private final CodeService codeService;

    // Repository
    private final PetRepository petRepository;

    public Pet register(Pet pet) {
        return petRepository.save(pet.setBreedName(codeService.getCode(pet.getBreedCode()).getCodeName()));
    }

    @Transactional
    public void changePetInfo(ModifyPetRecord modifyPetRecord,User user){
        Optional.ofNullable(modifyPetRecord.name()).ifPresent(user.getPet()::setName);
        Optional.ofNullable(modifyPetRecord.birthday()).ifPresent(user.getPet()::setBirthday);
        Optional.ofNullable(modifyPetRecord.breed()).ifPresent(user.getPet()::setBreedCode);
    }
}
