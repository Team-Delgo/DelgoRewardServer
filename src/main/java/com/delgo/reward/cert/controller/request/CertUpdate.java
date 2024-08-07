package com.delgo.reward.cert.controller.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
public record CertUpdate(
        @Schema(description = "유저 고유 아이디")
        @NotNull Integer userId,
        @Schema(description = "인증 고유 아이디")
        @NotNull Integer certificationId,
        @Schema(description = "수정할 내용")
        @NotBlank String description,
        @Schema(description = "주소 숨김 여부")
        @NotNull Boolean isHideAddress) {
}