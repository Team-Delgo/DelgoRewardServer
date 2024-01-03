package com.delgo.reward.user.controller.request;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record PetUpdate(@NotNull String email,
                        String name,
                        LocalDate birthday,
                        String breed) {
}
