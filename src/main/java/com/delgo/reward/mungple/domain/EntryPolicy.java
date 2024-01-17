package com.delgo.reward.mungple.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(enumAsRef = true, description = """
        허용 상태:
        * `ALLOW` - 허용
        * `DENY` - 금지
        * `OUTDOOR` - 야외 허용
        """)
public enum EntryPolicy {
    ALLOW("ALLOW", "허용"),
    DENY("DENY", "금지"),
    OUTDOOR("OUTDOOR", "야외 허용");

    private final String code;
    private final String desc;
}
