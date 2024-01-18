package com.delgo.reward.user.response;

import com.delgo.reward.cert.repository.dto.UserVisitMungpleCountDTO;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.user.domain.Pet;
import com.delgo.reward.user.domain.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    void from() {
        // given
        LocalDate birthDay = LocalDate.now().minusYears(5);
        Pet pet = Pet.builder()
                .petId(1)
                .name("test pet")
                .breed("DT0056")
                .breedName("미니어처 핀셔")
                .birthday(birthDay)
                .build();
        User user = User.builder()
                .userId(1)
                .email("test@delgo.pet")
                .name("test name")
                .phoneNo("01062511583")
                .geoCode("101000")
                .address("경기도 성남시 분당구")
                .profile("photo1")
                .userSocial(UserSocial.D)
                .isNotify(true)
                .viewCount(9)
                .pet(pet)
                .build();

        // when
        UserResponse userResponse = UserResponse.from(user);

        // then
        Period expectedPeriod = Period.between(birthDay, LocalDate.now());
            // User
        assertThat(userResponse.getUserId()).isEqualTo(user.getUserId());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getNickname()).isEqualTo(user.getName());
        assertThat(userResponse.getPhoneNo()).isEqualTo(user.getPhoneNo());
        assertThat(userResponse.getGeoCode()).isEqualTo(user.getGeoCode());
        assertThat(userResponse.getAddress()).isEqualTo(user.getAddress());
        assertThat(userResponse.getProfile()).isEqualTo(user.getProfile());
        assertThat(userResponse.getUserSocial()).isEqualTo(user.getUserSocial());
        assertThat(userResponse.getIsNotify()).isEqualTo(user.getIsNotify());
        assertThat(userResponse.getViewCount()).isEqualTo(user.getViewCount());
            // Pet
        assertThat(userResponse.getPetId()).isEqualTo(pet.getPetId());
        assertThat(userResponse.getPetName()).isEqualTo(pet.getName());
        assertThat(userResponse.getBreed()).isEqualTo(pet.getBreed());
        assertThat(userResponse.getBreedName()).isEqualTo(pet.getBreedName());
        assertThat(userResponse.getBirthday()).isEqualTo(pet.getBirthday());
        assertThat(userResponse.getYearOfPetAge()).isEqualTo(expectedPeriod.getYears());
        assertThat(userResponse.getMonthOfPetAge()).isEqualTo(expectedPeriod.getMonths());
    }

    @Test
    void fromAccount() {
        // given
        LocalDate birthDay = LocalDate.now().minusYears(5);
        Pet pet = Pet.builder()
                .petId(1)
                .name("test pet")
                .breed("DT0056")
                .breedName("미니어처 핀셔")
                .birthday(birthDay)
                .build();
        User user = User.builder()
                .userId(1)
                .email("test@delgo.pet")
                .name("test name")
                .phoneNo("01062511583")
                .geoCode("101000")
                .address("경기도 성남시 분당구")
                .profile("photo1")
                .userSocial(UserSocial.D)
                .isNotify(true)
                .viewCount(9)
                .pet(pet)
                .build();

        Map<CategoryCode, Integer> activityMapByCategoryCode = Map.of(CategoryCode.CA0001, 5, CategoryCode.CA0002, 9);
        List<UserVisitMungpleCountDTO> top3VisitedMungpleList = List.of(
                UserVisitMungpleCountDTO.builder().mungpleId(1).visitCount(5L).build(),
                UserVisitMungpleCountDTO.builder().mungpleId(2).visitCount(4L).build());

        // when
        UserResponse userResponse = UserResponse.fromAccount(user, activityMapByCategoryCode, top3VisitedMungpleList);

        // then
        Period expectedPeriod = Period.between(birthDay, LocalDate.now());
        // User
        assertThat(userResponse.getUserId()).isEqualTo(user.getUserId());
        assertThat(userResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userResponse.getNickname()).isEqualTo(user.getName());
        assertThat(userResponse.getPhoneNo()).isEqualTo(user.getPhoneNo());
        assertThat(userResponse.getGeoCode()).isEqualTo(user.getGeoCode());
        assertThat(userResponse.getAddress()).isEqualTo(user.getAddress());
        assertThat(userResponse.getProfile()).isEqualTo(user.getProfile());
        assertThat(userResponse.getUserSocial()).isEqualTo(user.getUserSocial());
        assertThat(userResponse.getIsNotify()).isEqualTo(user.getIsNotify());
        assertThat(userResponse.getViewCount()).isEqualTo(user.getViewCount());
        // Pet
        assertThat(userResponse.getPetId()).isEqualTo(pet.getPetId());
        assertThat(userResponse.getPetName()).isEqualTo(pet.getName());
        assertThat(userResponse.getBreed()).isEqualTo(pet.getBreed());
        assertThat(userResponse.getBreedName()).isEqualTo(pet.getBreedName());
        assertThat(userResponse.getBirthday()).isEqualTo(pet.getBirthday());
        assertThat(userResponse.getYearOfPetAge()).isEqualTo(expectedPeriod.getYears());
        assertThat(userResponse.getMonthOfPetAge()).isEqualTo(expectedPeriod.getMonths());

        assertThat(userResponse.getActivityMapByCategoryCode()).isEqualTo(activityMapByCategoryCode);
        assertThat(userResponse.getTop3VisitedMungpleList()).isEqualTo(top3VisitedMungpleList);
    }

    @Test
    void fromOther() {
        // given
        LocalDate birthDay = LocalDate.now().minusYears(5);
        Pet pet = Pet.builder()
                .petId(1)
                .name("test pet")
                .breed("DT0056")
                .breedName("미니어처 핀셔")
                .birthday(birthDay)
                .build();
        User user = User.builder()
                .userId(1)
                .email("test@delgo.pet")
                .name("test name")
                .phoneNo("01062511583")
                .geoCode("101000")
                .address("경기도 성남시 분당구")
                .profile("photo1")
                .userSocial(UserSocial.D)
                .isNotify(true)
                .viewCount(9)
                .pet(pet)
                .build();

        Map<CategoryCode, Integer> activityMapByCategoryCode = Map.of(CategoryCode.CA0001, 5, CategoryCode.CA0002, 9);
        List<UserVisitMungpleCountDTO> top3VisitedMungpleList = List.of(
                UserVisitMungpleCountDTO.builder().mungpleId(1).visitCount(5L).build(),
                UserVisitMungpleCountDTO.builder().mungpleId(2).visitCount(4L).build());

        // when
        UserResponse userResponse = UserResponse.fromOther(user, activityMapByCategoryCode, top3VisitedMungpleList);

        // then
        Period expectedPeriod = Period.between(birthDay, LocalDate.now());
        // User
        assertThat(userResponse.getUserId()).isEqualTo(user.getUserId());
        assertThat(userResponse.getNickname()).isEqualTo(user.getName());
        assertThat(userResponse.getProfile()).isEqualTo(user.getProfile());
        assertThat(userResponse.getViewCount()).isEqualTo(user.getViewCount());
        // Pet
        assertThat(userResponse.getPetId()).isEqualTo(pet.getPetId());
        assertThat(userResponse.getPetName()).isEqualTo(pet.getName());
        assertThat(userResponse.getBreed()).isEqualTo(pet.getBreed());
        assertThat(userResponse.getBreedName()).isEqualTo(pet.getBreedName());
        assertThat(userResponse.getBirthday()).isEqualTo(pet.getBirthday());
        assertThat(userResponse.getYearOfPetAge()).isEqualTo(expectedPeriod.getYears());
        assertThat(userResponse.getMonthOfPetAge()).isEqualTo(expectedPeriod.getMonths());

        assertThat(userResponse.getActivityMapByCategoryCode()).isEqualTo(activityMapByCategoryCode);
        assertThat(userResponse.getTop3VisitedMungpleList()).isEqualTo(top3VisitedMungpleList);
    }

    @Test
    void fromSearch() {
        // given
        LocalDate birthDay = LocalDate.now().minusYears(5);
        Pet pet = Pet.builder()
                .petId(1)
                .name("test pet")
                .breed("DT0056")
                .breedName("미니어처 핀셔")
                .birthday(birthDay)
                .build();
        User user = User.builder()
                .userId(1)
                .email("test@delgo.pet")
                .name("test name")
                .phoneNo("01062511583")
                .geoCode("101000")
                .address("경기도 성남시 분당구")
                .profile("photo1")
                .userSocial(UserSocial.D)
                .isNotify(true)
                .viewCount(9)
                .pet(pet)
                .build();

        // when
        UserResponse userResponse = UserResponse.fromSearch(user);

        // then
        Period expectedPeriod = Period.between(birthDay, LocalDate.now());
        // User
        assertThat(userResponse.getUserId()).isEqualTo(user.getUserId());
        assertThat(userResponse.getNickname()).isEqualTo(user.getName());
        assertThat(userResponse.getProfile()).isEqualTo(user.getProfile());
        // Pet
        assertThat(userResponse.getPetId()).isEqualTo(pet.getPetId());
        assertThat(userResponse.getPetName()).isEqualTo(pet.getName());
        assertThat(userResponse.getBreed()).isEqualTo(pet.getBreed());
        assertThat(userResponse.getBreedName()).isEqualTo(pet.getBreedName());
        assertThat(userResponse.getBirthday()).isEqualTo(pet.getBirthday());
        assertThat(userResponse.getYearOfPetAge()).isEqualTo(expectedPeriod.getYears());
        assertThat(userResponse.getMonthOfPetAge()).isEqualTo(expectedPeriod.getMonths());
    }

    @Test
    void fromSearchList() {
        // given
        User user1 = User.builder().userId(1).pet(Pet.builder().birthday(LocalDate.now()).build()).build();
        User user2 = User.builder().userId(2).pet(Pet.builder().birthday(LocalDate.now()).build()).build();
        List<User> userList = List.of(user1, user2);

        // when
        List<UserResponse> userResponseList = UserResponse.fromSearchList(userList);

        // then
        assertThat(userResponseList.size()).isEqualTo(2);
        assertThat(userResponseList.get(0).getUserId()).isEqualTo(userList.get(0).getUserId());
        assertThat(userResponseList.get(1).getUserId()).isEqualTo(userList.get(1).getUserId());
    }
}