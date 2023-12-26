package com.delgo.reward.record.signup;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record SignUpRecord(
        @Schema(description = "이름", required = true)
        @NotBlank String userName,
        @Schema(description = "이메일", required = true)
        @NotBlank String email,
        @Schema(description = "비밀번호", required = true)
        @NotBlank String password,
        @Schema(description = "전화번호", required = true)
        @NotBlank String phoneNo,
        @Schema(description = "지역 코드", required = true)
        @NotBlank String geoCode,
        @Schema(description = "상위 지역 코드", required = true)
        @NotBlank String pGeoCode,
        @Schema(description = "펫 이름", required = true)
        @NotBlank String petName,
        @Schema(description = "품종 코드", required = true)
        @NotBlank String breed,
        @Schema(description = "펫의 생일", required = true)
        @NotNull LocalDate birthday) {
}
