package com.delgo.reward.dto;

import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.domain.user.UserSocial;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthSignUpDTO {
    @NotNull private String email; // apple 로그인 시 email 빈 값으로 들어옴
    @NotBlank private String userName;
    @NotBlank private String phoneNo;
    @NotBlank private String geoCode;
    @JsonProperty("pGeoCode")
    @NotBlank private String pGeoCode;
    @NotBlank private String petName;
    @NotNull private String breed;
    @NotNull private LocalDate birthday;
    @NotNull private UserSocial userSocial;

    private String appleUniqueNo; // apple 로그인 시에만 @NotNull 넣지 않음.
    private String socialId; // kakao Or Naver 고유 Id

    public User makeUserSocial(UserSocial userSocial, String address) {
        switch (userSocial) {
            case A: return User.builder()
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
            case K: return User.builder()
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
            default: return User.builder()
                        .name(userName)
                        .email(email)
                        .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                        .userSocial(userSocial)
                        .address(address)
                        .geoCode(geoCode)
                        .pGeoCode(pGeoCode)
                        .isNotify(true)
                        .build();
        }
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
