package com.delgo.reward.user.controller.request;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
public record PetUpdate(@NotNull String email,
                        String name,
                        LocalDate birthday,
                        String breed) {
}
