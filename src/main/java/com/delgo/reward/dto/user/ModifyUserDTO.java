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
    private String profileUrl;
    private String name;
    @NotNull
    private String email;
    private String geoCode;
    @JsonProperty("pGeoCode")
    private String pGeoCode;
}
