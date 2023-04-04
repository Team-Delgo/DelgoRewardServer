package com.delgo.reward.record.user;

import javax.validation.constraints.NotNull;

public record ResetPasswordRecord(@NotNull String email,
                                  @NotNull String newPassword) {
}
