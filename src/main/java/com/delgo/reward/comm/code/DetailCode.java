package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DetailCode {

    ALLOW("ALLOW", "허용"),
    DENY("DENY", "금지"),
    OUTDOOR("OUTDOOR", "야외 허용");

    private final String code;
    private final String desc;

    DetailCode(String code, String value) {
        this.code = code;
        this.desc = value;
    }

    public String getCode(){
        return this.code;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static DetailCode from(String s) {
        return DetailCode.valueOf(s);
    }
}
