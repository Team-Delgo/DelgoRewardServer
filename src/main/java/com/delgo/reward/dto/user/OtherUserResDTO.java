package com.delgo.reward.dto.user;


import com.delgo.reward.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Slf4j
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OtherUserResDTO {
    // User
    private int userId;
    private String nickname;
    private String profile;
    private int viewCount;

    // Pet
    private int petId;
    private String petName;
    private String breed;
    private String breedName;
    private int yearOfPetAge;
    private int monthOfPetAge;

    public OtherUserResDTO(User user){
        // User
        userId = user.getUserId();
        nickname = user.getName();
        profile = user.getProfile();
        viewCount = user.getViewCount();

        // Pet
        petId = user.getPet().getPetId();
        petName = user.getPet().getName();
        breed = user.getPet().getBreed();
        breedName = user.getPet().getBreedName();

        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        yearOfPetAge = period.getYears();
        monthOfPetAge = period.getMonths();
    }
}