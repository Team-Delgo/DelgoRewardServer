package com.delgo.reward.service;


import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.record.user.ModifyPetRecord;
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

    // Service
    private final UserService userService;
    private final CodeService codeService;

    // Repository
    private final PetRepository petRepository;

    public Pet register(Pet pet) {
        return petRepository.save(pet).setBreedName(codeService.getCode(pet.getBreedCode()).getCodeName());
    }

    @Transactional
    public void changePetInfo(ModifyPetRecord modifyPetRecord){
        User user = userService.getUserByEmail(modifyPetRecord.email());
        int userId = user.getUserId();
        Pet originPet = getPetByUserId(userId);

        if (modifyPetRecord.name() != null)
            originPet.setName(modifyPetRecord.name());

        if(modifyPetRecord.birthday() != null)
            originPet.setBirthday(modifyPetRecord.birthday());

        if (modifyPetRecord.breed() != null)
            originPet.setBreedCode(modifyPetRecord.breed());

        petRepository.save(originPet);
    }

    public Pet getPetByUserId(int userId) {
        Pet pet = petRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND PET"));
        pet.setBreedName(codeService.getCode(pet.getBreedCode()).getCodeName()); // 견종 이름 추가
        return pet;
    }
}
