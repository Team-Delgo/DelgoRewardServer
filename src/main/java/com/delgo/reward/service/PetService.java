package com.delgo.reward.service;


import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.record.user.ModifyPetRecord;
import com.delgo.reward.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        return petRepository.save(pet.setBreedName(codeService.getByCode(pet.getBreed()).getCodeName()));
    }

    @Transactional
    public void changePetInfo(ModifyPetRecord modifyPetRecord,User user){
        Optional.ofNullable(modifyPetRecord.name()).ifPresent(user.getPet()::setName);
        Optional.ofNullable(modifyPetRecord.birthday()).ifPresent(user.getPet()::setBirthday);
        Optional.ofNullable(modifyPetRecord.breed()).ifPresent(breed ->{
            user.getPet().setBreed(breed);
            user.getPet().setBreedName(codeService.getByCode(breed).getCodeName());
        });
    }

    // 단순 DB 데이터 적치용
    public void fillBreedName(){
        List<Pet> pets = petRepository.findAll();
        for(Pet pet : pets){
            pet.setBreedName(codeService.getByCode(pet.getBreed()).getCodeName());
        }
    }
}