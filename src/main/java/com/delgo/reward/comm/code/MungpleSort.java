package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
@Schema(enumAsRef = true, description = """
        멍플 정렬 옵션:
        * `NEWEST` - 최신 순
        * `OLDEST` - 오래된 순
        * `DISTANCE` - 거리 순
        * `CERT` - 인증 많은 순
        * `BOOKMARK` - 저장 많은 순
        """)
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