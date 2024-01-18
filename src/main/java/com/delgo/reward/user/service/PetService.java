package com.delgo.reward.user.service;


import com.delgo.reward.code.domain.Code;
import com.delgo.reward.code.service.CodeService;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.user.domain.Pet;
import com.delgo.reward.user.controller.request.PetUpdate;
import com.delgo.reward.user.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return petRepository.save(pet);
    }

    @Transactional
    public Pet update(PetUpdate petUpdate) {
        Pet pet = userQueryService.getOneByEmail(petUpdate.email()).getPet();
        Code breedCode = codeService.getOneByCode(petUpdate.breed());

        return petRepository.save(pet.update(petUpdate, breedCode.getCodeName()));
    }

    @Transactional
    public void deleteByUserId(int userId){
        petRepository.deleteByUserUserId(userId);
    }

    public Pet getOneByUserId(int userId){
        return petRepository.findByUserUserId(userId)
                .orElseThrow(() -> new NotFoundDataException("[Pet] userId : " + userId));
    }
}