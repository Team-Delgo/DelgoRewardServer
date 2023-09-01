package com.delgo.reward.dto.user;


import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserResDTO {
    // User Data
    private int userId;
    private String email;
    private String nickname;
    private String phoneNo; // 보안 필요함.
    private String geoCode; // 보안 필요함.
    private String address;
    private String profile;
    private UserSocial userSocial;
    private Boolean isNotify;

    // Pet Data
    private int petId;
    private String petName;
    private String breed;
    private String breedName;
    private LocalDate birthday;

    private int yearOfPetAge;
    private int monthOfPetAge;

    @JsonFormat(pattern="yyyy.MM.dd/HH:mm/E")
    private LocalDateTime registDt;

    public UserResDTO(User user){
        // User
        userId = user.getUserId();
        email = user.getEmail();
        nickname = user.getName();
        phoneNo = user.getPhoneNo();
        geoCode = user.getGeoCode();
        address = user.getAddress();
        profile = user.getProfile();
        userSocial = user.getUserSocial();
        isNotify = user.getIsNotify();

        // Pet
        petId = user.getPet().getPetId();
        petName = user.getPet().getName();
        breed = user.getPet().getBreed();
        breedName = user.getPet().getBreedName();
        birthday = user.getPet().getBirthday();
        registDt = user.getRegistDt();

        Period period = Period.between(user.getPet().getBirthday(), LocalDate.now());
        yearOfPetAge = period.getYears();
        monthOfPetAge = period.getMonths();
    }
}