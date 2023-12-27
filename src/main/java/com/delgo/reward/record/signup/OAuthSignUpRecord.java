package com.delgo.reward.record.signup;

import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.comm.code.UserSocial;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record OAuthSignUpRecord(
        @Schema(description = "이메일", required = true)
        @NotNull String email,
        @Schema(description = "유저 이름", required = true)
        @NotBlank String userName,
        @Schema(description = "전화번호", required = true)
        @NotBlank String phoneNo,
        @Schema(description = "지역 코드", required = true)
        @NotBlank String geoCode,
        @Schema(description = "상위 지역 코드", required = true)
        @NotBlank @JsonProperty("pGeoCode") String pGeoCode,
        @Schema(description = "펫 이름", required = true)
        @NotBlank String petName,
        @Schema(description = "품종 코드", required = true)
        @NotNull String breed,
        @Schema(description = "펫의 생일", required = true)
        @NotNull LocalDate birthday,
        @Schema(description = "가입 소셜 정보", required = true)
        @NotNull UserSocial userSocial,
        @Schema(description = "Apple 고유 번호")
        String appleUniqueNo,
        @Schema(description = "KakaoId (Kakako 로그아웃 시 필요)")
        String socialId,
        @Schema(description = "성별")
        String gender,
        @Schema(description = "나이")
        Integer age
) {
    public User makeUser(UserSocial userSocial, String address) {
        return switch (userSocial) {
            case A -> User.builder()
                    .name(userName)
                    .email(null)
                    .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                    .userSocial(userSocial)
                    .address(address)
                    .geoCode(geoCode)
                    .pGeoCode(pGeoCode)
                    .isNotify(true)
                    .appleUniqueNo(appleUniqueNo)
                    .roles("ROLE_USER")
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
                    .age(age)
                    .gender(gender)
                    .roles("ROLE_USER")
                    .build();
            default -> User.builder()
                    .name(userName)
                    .email(email)
                    .phoneNo(phoneNo.replaceAll("[^0-9]", ""))
                    .userSocial(userSocial)
                    .address(address)
                    .geoCode(geoCode)
                    .pGeoCode(pGeoCode)
                    .age(age)
                    .gender(gender)
                    .isNotify(true)
                    .roles("ROLE_USER")
                    .build();
        };
    }

    public Pet makePet(User user){
        return Pet.builder()
                .name(petName)
                .breed(breed)
                .birthday(birthday)
                .user(user)
                .build();
    }
}