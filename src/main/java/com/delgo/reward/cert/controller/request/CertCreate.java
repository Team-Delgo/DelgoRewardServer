package com.delgo.reward.cert.controller.request;

import com.delgo.reward.comm.code.CategoryCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Builder
public record CertCreate(
        @NotNull @Schema(description = "유저 고유 아이디")
        Integer userId,
        @NotNull @Schema(description = "장소 명")
        String placeName,
        @NotBlank @Schema(description = "인증 내용")
        String description,
        @NotNull @Schema(description = "멍플 고유 아이디 (안 멍플일 경우 = 0 )")
        Integer mungpleId,
        @NotNull @Schema(description = "인증 카테고리 코드")
        CategoryCode categoryCode,
        @Schema(description = "위도")
        String latitude,
        @Schema(description = "경도")
        String longitude,
        @NotNull @Schema(description = "주소 숨김 여부")
        Boolean isHideAddress) {
}