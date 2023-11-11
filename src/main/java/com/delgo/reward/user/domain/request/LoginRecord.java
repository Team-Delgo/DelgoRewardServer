package com.delgo.reward.user.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record LoginRecord(
        @NotNull @Schema(description = "로그인 시도 이메일")
        String email,
        @NotNull @Schema(description = "로그인 시도 비밀번호")
        String password) {
}
