package com.delgo.reward.user.controller.request;

import com.delgo.reward.comm.code.UserSocial;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
public record OAuthCreate(
        @NotNull @Schema(description = "이메일", required = true)
        String email,
        @NotBlank @Schema(description = "유저 이름", required = true)
        String userName,
        @NotBlank @Schema(description = "전화번호", required = true)
        String phoneNo,
        @NotBlank @Schema(description = "지역 코드", required = true)
        String geoCode,
        @NotBlank @JsonProperty("pGeoCode") @Schema(description = "상위 지역 코드", required = true)
        String pGeoCode,
        @NotBlank @Schema(description = "펫 이름", required = true)
        String petName,
        @NotNull @Schema(description = "품종 코드", required = true)
        String breed,
        @NotNull @Schema(description = "펫의 생일", required = true)
        LocalDate birthday,
        @Schema(description = "가입 소셜 정보", required = true)
        UserSocial userSocial,
        @Schema(description = "Apple 고유 번호")
        String appleUniqueNo,
        @Schema(description = "KakaoId (Kakako 로그아웃 시 필요)")
        String socialId,
        @Schema(description = "성별")
        String gender,
        @Schema(description = "나이")
        Integer age
) {
}