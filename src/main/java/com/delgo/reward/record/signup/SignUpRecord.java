package com.delgo.reward.record.signup;

import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record SignUpRecord(
        @NotBlank String userName,
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String phoneNo,
        @NotBlank String geoCode,
        @NotBlank String pGeoCode,
        @NotBlank String petName,
        @NotBlank String breed,
        @NotNull LocalDate birthday) {

    public User makeUser(String password, String address) {
        return User.builder()
                .name(userName)
                .email(email)
                .password(password)
                .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                .userSocial(UserSocial.D)
                .address(address)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .isNotify(true)
                .build();
    }

    public Pet makePet(int userId) {
        return Pet.builder()
                .name(petName)
                .breed(breed)
                .birthday(birthday)
                .userId(userId)
                .build();
    }
}
