package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum ParkingLimitCode {
    NONE(0, "0"),
    ONE(1, "1"),
    TWO(2, "2"),
    THREE(3, "3"),
    MANY(999, "여러대");

    private final Integer code;
    private final String value;

    ParkingLimitCode(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static ParkingLimitCode from(String s) {
        return ParkingLimitCode.valueOf(s);
    }
}