package com.delgo.reward.common.controller.request;

import javax.validation.constraints.NotNull;

public record LoginRecord(@NotNull String email,
                          @NotNull String password) {
}
