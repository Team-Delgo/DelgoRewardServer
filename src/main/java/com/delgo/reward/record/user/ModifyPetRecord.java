package com.delgo.reward.record.user;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record ModifyPetRecord(@NotNull String email,
                              String name,
                              LocalDate birthday,
                              String breed) {
}
