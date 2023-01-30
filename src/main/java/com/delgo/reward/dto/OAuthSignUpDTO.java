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
    private String email;
    @NotBlank
    private String userName;
    @NotBlank
    private String phoneNo;
    @NotBlank
    private String geoCode;
    @NotBlank
    @JsonProperty("pGeoCode")
    private String pGeoCode;
    @NotBlank
    private String petName;
    @NotNull
    private String breed;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private UserSocial userSocial;

    // KAKAO, NAVER 때는 필요없어서 @NotNull 넣지 않음.
    private String appleUniqueNo;

    public User makeUserSocial(UserSocial userSocial, String address){
        return User.builder()
                .name(userName)
                .email(email)
                .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                .userSocial(userSocial)
                .address(address)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .build();
    }

    public User makeUserApple(String appleUniqueNo, String address){
        return User.builder()
                .name(userName)
                .email(email)
                .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                .userSocial(UserSocial.A)
                .address(address)
                .geoCode(geoCode)
                .pGeoCode(pGeoCode)
                .build();
    }

    public Pet makePet(){
        return Pet.builder()
                .name(petName)
                .breed(breed)
                .birthday(birthday)
                .build();
    }
}
