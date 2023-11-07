package com.delgo.reward.common.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(enumAsRef = true, description = """
        CODE TYPE:
        * `GEO` - 지역 코드
        * `BREED` - 품종 코드
        """)
public enum CodeType {
    geo("geo", "지역 코드"),
    breed("breed", "품종 코드");

    private final String value;
    private final String desc;

    CodeType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static CodeType from(String s) {
        return CodeType.valueOf(s);
    }
}
