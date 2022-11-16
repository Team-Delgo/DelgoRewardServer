package com.delgo.reward.dto;

import com.delgo.reward.domain.pet.PetSize;
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
    private PetSize petSize;
    @NotNull
    private LocalDate birthday;
    @NotNull
    private UserSocial userSocial;

    // KAKAO, NAVER 때는 필요없어서 @NotNull 넣지 않음.
    private String appleUniqueNo;
}
