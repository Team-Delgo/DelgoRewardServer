package com.delgo.reward.user.service;

import com.delgo.reward.record.user.ModifyPetRecord;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.user.domain.Pet;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.port.PetRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Builder
@Service
@RequiredArgsConstructor
public class PetService {

    // Service
    private final CodeService codeService;

    // Repository
    private final PetRepository petRepository;

    @Transactional
    public Pet register(Pet pet) {
        pet.setBreedName(codeService.getCode(pet.getBreed()).getCodeName());
        return petRepository.save(pet);
    }

    @Transactional
    public void changePetInfo(ModifyPetRecord modifyPetRecord, User user){
        Optional.ofNullable(modifyPetRecord.name()).ifPresent(user.getPet()::setName);
        Optional.ofNullable(modifyPetRecord.birthday()).ifPresent(user.getPet()::setBirthday);
        Optional.ofNullable(modifyPetRecord.breed()).ifPresent(breed ->{
            user.getPet().setBreed(breed);
            user.getPet().setBreedName(codeService.getCode(breed).getCodeName());
        });
    }

    // 단순 DB 데이터 적치용
    public void fillBreedName(){
        List<Pet> pets = petRepository.findAll();
        for(Pet pet : pets){
            pet.setBreedName(codeService.getCode(pet.getBreed()).getCodeName());
        }
    }
}