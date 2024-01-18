package com.delgo.reward.user.domain;

import com.delgo.reward.user.controller.request.OAuthCreate;
import com.delgo.reward.user.controller.request.PetUpdate;
import com.delgo.reward.user.controller.request.UserCreate;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class PetTest {

    @Test
    void from() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .petName("멍멍이")
                .breed("BREED001")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        User user = User.builder().userId(1).build();

        // when
        Pet pet = Pet.from(userCreate, user);

        // then
        assertThat(pet.getName()).isEqualTo(userCreate.petName());
        assertThat(pet.getBreed()).isEqualTo(userCreate.breed());
        assertThat(pet.getBirthday()).isEqualTo(userCreate.birthday());
        assertThat(pet.getUser().getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void fromOAuth() {
        // given
        OAuthCreate oAuthCreate = OAuthCreate.builder()
                .petName("멍멍이")
                .breed("BREED001")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        User user = User.builder().userId(1).build();

        // when
        Pet pet = Pet.from(oAuthCreate, user);

        // then
        assertThat(pet.getName()).isEqualTo(oAuthCreate.petName());
        assertThat(pet.getBreed()).isEqualTo(oAuthCreate.breed());
        assertThat(pet.getBirthday()).isEqualTo(oAuthCreate.birthday());
        assertThat(pet.getUser().getUserId()).isEqualTo(user.getUserId());
    }

    @Test
    void update() {
        // given
        Pet pet = Pet.builder().build();
        String breedName = "test breed name";
        PetUpdate petUpdate = PetUpdate.builder().
                name("update name")
                .breed("TEST001")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();

        // when
        Pet updatedPet = pet.update(petUpdate, breedName);

        // then
        assertThat(updatedPet.getName()).isEqualTo(petUpdate.name());
        assertThat(updatedPet.getBreed()).isEqualTo(petUpdate.breed());
        assertThat(updatedPet.getBreedName()).isEqualTo(breedName);
        assertThat(updatedPet.getBirthday()).isEqualTo(petUpdate.birthday());
    }

    @Test
    void setName() {
        // given
        Pet pet = new Pet();
        String name = "test name";

        // when
        pet.setName(name);

        // then
        assertThat(name).isEqualTo(pet.getName());
    }

    @Test
    void setBreed() {
        // given
        Pet pet = new Pet();
        String breed = "test breed";

        // when
        pet.setBreed(breed);

        // then
        assertThat(breed).isEqualTo(pet.getBreed());
    }

    @Test
    void setBreedName() {
        // given
        Pet pet = new Pet();
        String breedName = "test breedName";

        // when
        pet.setBreedName(breedName);

        // then
        assertThat(breedName).isEqualTo(pet.getBreedName());
    }

    @Test
    void setBirthday() {
        // given
        Pet pet = new Pet();
        LocalDate birthday = LocalDate.of(2022, 10, 1);

        // when
        pet.setBirthday(birthday);

        // then
        assertThat(birthday).isEqualTo(pet.getBirthday());
    }
}