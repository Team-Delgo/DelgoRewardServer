package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum GeoCode {
    C101180("101180", "송파구");

    private final String geoCode;
    private final String codeDesc;

    GeoCode(String geoCode, String codeDesc){
        this.geoCode = geoCode;
        this.codeDesc = codeDesc;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static PGeoCode from(String s) {
        return PGeoCode.valueOf(s);
    }
}
