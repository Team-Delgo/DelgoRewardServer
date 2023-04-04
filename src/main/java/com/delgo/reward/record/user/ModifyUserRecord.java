package com.delgo.reward.record.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public record ModifyUserRecord(@NotNull Integer userId,
                               String name,
                               String profileUrl,
                               String geoCode,
                               @JsonProperty("pGeoCode") String pGeoCode) {
}
