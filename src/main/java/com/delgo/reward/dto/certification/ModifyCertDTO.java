package com.delgo.reward.dto.certification;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCertDTO {
    @NotNull private Integer userId;
    @NotNull private Integer certId;
    @NotBlank private String description;
}