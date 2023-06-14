package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReactionCode {
    HELPER("Helper", "도움돼요"),
    CUTE("CUTE", "귀여워요");

    private final String code;
    private final String desc;
    ReactionCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode(){
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static ReactionCode from(String s) {
        return ReactionCode.valueOf(s);
    }
}
