package com.delgo.reward.dto.user;


import com.delgo.reward.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.Period;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchUserResDTO {
    // User
    private int userId;
    private String nickname;
    private String profile;

    // Pet
    private int petId;
    private String petName;
    private String breed;
    private String breedName;
    private int petAge;

    public SearchUserResDTO(User user){
        userId = user.getUserId();
        nickname = user.getName();
        profile = user.getProfile();

        petId = user.getPet().getPetId();
        petName = user.getPet().getName();
        breed = user.getPet().getBreed();
        breedName = user.getPet().getBreedName();
        petAge = Period.between(user.getPet().getBirthday(),LocalDate.now()).getYears();
    }
}