package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;


@Getter
public enum MungpleSort {
    NEWEST("NEWEST","최신 순"),
    OLDEST("OLDEST","오래된 순"),
    DISTANCE("DISTANCE","거리 순"),
    CERT("CERT","인증 많은 순"),
    BOOKMARK("BOOKMARK","저장 많은 순");

    private final String code;
    private final String desc;

    MungpleSort(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static MungpleSort from(String s) {
        return MungpleSort.valueOf(s);
    }
}