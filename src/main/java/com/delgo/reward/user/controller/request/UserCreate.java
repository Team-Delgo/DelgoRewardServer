package com.delgo.reward.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
public record UserCreate(

        @NotBlank @Schema(description = "이름", required = true)
        String userName,
        @NotBlank @Schema(description = "이메일", required = true)
        String email,
        @NotBlank @Schema(description = "비밀번호", required = true)
        String password,
        @NotBlank @Schema(description = "전화번호", required = true)
        String phoneNo,
        @NotBlank @Schema(description = "지역 코드", required = true)
        String geoCode,
        @NotBlank @Schema(description = "상위 지역 코드", required = true)
        String pGeoCode,
        @NotBlank @Schema(description = "펫 이름", required = true)
        String petName,
        @NotBlank @Schema(description = "품종 코드", required = true)
        String breed,
        @NotNull @Schema(description = "펫의 생일", required = true)
        LocalDate birthday) {
}
