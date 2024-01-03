package com.delgo.reward.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record PasswordUpdate(
        @Schema(description = "이메일")
        @NotNull String email,
        @Schema(description = "새 비밀번호")
        @NotNull String newPassword) {
}
