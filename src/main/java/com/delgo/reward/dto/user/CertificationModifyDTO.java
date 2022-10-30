package com.delgo.reward.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificationModifyDTO {
    @NotNull private Integer certificationId;
    @NotBlank private String description;
}