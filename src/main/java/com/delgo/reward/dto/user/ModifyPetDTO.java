package com.delgo.reward.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class ModifyPetDTO {
    @NotNull
    private String email;
    private String name;
    private LocalDate birthday;
    private String breed;
}
