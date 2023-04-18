package com.delgo.reward.record.signup;

import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record OAuthSignUpRecord(
        @NotNull String email,
        @NotBlank String userName,
        @NotBlank String phoneNo,
        @NotBlank String geoCode,
        @NotBlank @JsonProperty("pGeoCode") String pGeoCode,
        @NotBlank String petName,
        @NotNull String breed,
        @NotNull LocalDate birthday,
        @NotNull UserSocial userSocial,
        String appleUniqueNo,
        String socialId
) {

    public User makeUserSocial(UserSocial userSocial, String address) {
        return switch (userSocial) {
            case A -> User.builder()
                    .name(userName)
                    .email(email)
                    .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                    .userSocial(userSocial)
                    .address(address)
                    .geoCode(geoCode)
                    .pGeoCode(pGeoCode)
                    .isNotify(true)
                    .appleUniqueNo(appleUniqueNo)
                    .build();
            case K -> User.builder()
                    .name(userName)
                    .email(email)
                    .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                    .userSocial(userSocial)
                    .address(address)
                    .geoCode(geoCode)
                    .pGeoCode(pGeoCode)
                    .isNotify(true)
                    .kakaoId(socialId)
                    .build();
            default -> User.builder()
                    .name(userName)
                    .email(email)
                    .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                    .userSocial(userSocial)
                    .address(address)
                    .geoCode(geoCode)
                    .pGeoCode(pGeoCode)
                    .isNotify(true)
                    .build();
        };
    }

    public Pet makePet(int userId){
        return Pet.builder()
                .name(petName)
                .breed(breed)
                .birthday(birthday)
                .userId(userId)
                .build();
    }
}