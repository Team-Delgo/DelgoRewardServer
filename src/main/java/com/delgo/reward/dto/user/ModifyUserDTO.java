package com.delgo.reward.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@Builder
public class ModifyUserDTO {
    @NotNull
    private Integer userId;
    private String name;
    private String profileUrl;

    private String geoCode;
    @JsonProperty("pGeoCode")
    private String pGeoCode;
}
