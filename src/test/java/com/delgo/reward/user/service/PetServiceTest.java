package com.delgo.reward.user.service;

import com.delgo.reward.code.domain.Code;
import com.delgo.reward.code.service.CodeService;
import com.delgo.reward.comm.exception.NotFoundDataException;
import com.delgo.reward.user.controller.request.PetUpdate;
import com.delgo.reward.user.domain.Pet;
import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PetServiceTest {
    @Autowired
    PetService petService;
    @Autowired
    CodeService codeService;

    @Test
    @Transactional
    void create() {
        // given
        Pet pet = Pet.builder()
                .name("멍멍이")
                .breed("DT0001")
                .birthday(LocalDate.of(2020, 1, 1))
                .user(User.builder().userId(1).build())
                .build();
        Code breedCode = codeService.getOneByCode(pet.getBreed());
        pet.setBreedName(breedCode.getCodeName());

        // when
        Pet createdPet = petService.create(pet);

        // then
        assertThat(createdPet.getName()).isEqualTo(pet.getName());
        assertThat(createdPet.getBreed()).isEqualTo(pet.getBreed());
        assertThat(createdPet.getBreedName()).isEqualTo(pet.getBreedName());
        assertThat(createdPet.getBirthday()).isEqualTo(pet.getBirthday());
        assertThat(createdPet.getUser().getUserId()).isEqualTo(pet.getUser().getUserId());
    }

    @Test
    @Transactional
    void update() {
        // given
        PetUpdate petUpdate = PetUpdate.builder()
                .email("cjsrnr1114@naver.com")
                .name("update name")
                .breed("DT0001")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        Code breedCode = codeService.getOneByCode(petUpdate.breed());

        // when
        Pet updatedPet = petService.update(petUpdate);

        // then
        assertThat(updatedPet.getName()).isEqualTo(petUpdate.name());
        assertThat(updatedPet.getBreed()).isEqualTo(petUpdate.breed());
        assertThat(updatedPet.getBreedName()).isEqualTo(breedCode.getCodeName());
        assertThat(updatedPet.getBirthday()).isEqualTo(petUpdate.birthday());
        assertThat(updatedPet.getUser().getEmail()).isEqualTo(petUpdate.email());
    }

    @Test
    @Transactional
    void deleteByUserId() {
        // given
        int userId = 1;

        // when
        petService.deleteByUserId(userId);

        // then
        assertThatThrownBy(() -> {
            petService.getOneByUserId(userId);
        }).isInstanceOf(NotFoundDataException.class);
    }

    @Test
    void getOneByUserId() {
        // given
        int userId = 1;

        // when
        Pet pet = petService.getOneByUserId(userId);

        // then
        assertThat(pet.getUser().getUserId()).isEqualTo(userId);
    }
}