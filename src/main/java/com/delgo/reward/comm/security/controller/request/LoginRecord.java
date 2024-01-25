package com.delgo.reward.comm.security.controller.request;

import javax.validation.constraints.NotNull;

public record LoginRecord(@NotNull String email,
                          @NotNull String password) {
}
