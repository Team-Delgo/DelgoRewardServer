package com.delgo.reward.record.certification;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record ModifyCertRecord(
        @NotNull Integer userId,
        @NotNull Integer certificationId,
        @NotBlank String description) {
}