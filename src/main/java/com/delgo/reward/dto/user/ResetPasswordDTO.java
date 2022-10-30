package com.delgo.reward.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class ResetPasswordDTO {
    @NotNull
    private String email;
    @NotNull
    private String newPassword;
}
