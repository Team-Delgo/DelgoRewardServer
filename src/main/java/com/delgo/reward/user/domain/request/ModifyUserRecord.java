package com.delgo.reward.user.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public record ModifyUserRecord(
        @NotNull @Schema(description = "수정할 유저 아이디")
        Integer userId,
        @Schema(description = "유저 이름")
        String name,
        @Schema(description = "유저 지역 코드")
        String geoCode,
        @JsonProperty("pGeoCode") @Schema(description = "유저 부모 지역 코드")
        String pGeoCode) {
}
