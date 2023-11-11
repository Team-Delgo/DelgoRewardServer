package com.delgo.reward.user.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Builder
public record ModifyPetRecord(
        @NotNull @Schema(description = "펫 주인의 이메일")
        String email,
        @Schema(description = "펫 이름")
        String name,
        @Schema(description = "펫 생일")
        LocalDate birthday,
        @Schema(description = "펫 종류")
        String breed) {
}
