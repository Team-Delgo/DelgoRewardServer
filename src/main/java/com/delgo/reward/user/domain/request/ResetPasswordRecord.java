package com.delgo.reward.user.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public record ResetPasswordRecord(

        @NotNull @Schema(description = "이메일")
        String email,
        @NotNull @Schema(description = "새 비밀번호")
        String newPassword) {
}
