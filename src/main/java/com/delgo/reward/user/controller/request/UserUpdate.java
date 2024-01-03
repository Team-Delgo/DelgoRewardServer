package com.delgo.reward.user.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotNull;

@Builder
public record UserUpdate(@NotNull Integer userId,
                         String name,
                         String geoCode,
                         @JsonProperty("pGeoCode") String pGeoCode) {
}
