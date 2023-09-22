package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(enumAsRef = true, description =
        "허용 상태: \n" +
                "* `ALLOW` - 허용\n" +
                "* `DENY` - 금지\n" +
                "* `OUTDOOR` - 야외 허용\n")
public enum DetailCode {
    ALLOW("ALLOW", "허용"),
    DENY("DENY", "금지"),
    OUTDOOR("OUTDOOR", "야외 허용");

    private final String code;
    private final String desc;

    DetailCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static DetailCode from(String s) {
        return DetailCode.valueOf(s);
    }
}
