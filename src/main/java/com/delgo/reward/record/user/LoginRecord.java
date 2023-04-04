package com.delgo.reward.record.user;

import javax.validation.constraints.NotNull;

public record LoginRecord(@NotNull String email,
                          @NotNull String password) {
}
