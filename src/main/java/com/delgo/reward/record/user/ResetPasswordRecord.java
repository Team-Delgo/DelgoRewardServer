package com.delgo.reward.record.user;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public record ResetPasswordRecord(
        @Schema(description = "이메일")
        @NotNull String email,
        @Schema(description = "새 비밀번호")
        @NotNull String newPassword) {
}
