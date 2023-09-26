package com.delgo.reward.comm.code;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@Schema(enumAsRef = true, description = """
        카테고리 코드:
        * `CA0000` - 전체
        * `CA0001` - 산책
        * `CA0002` - 카페
        * `CA0003` - 식당
        * `CA0004` - 목욕
        * `CA0005` - 미용
        * `CA0006` - 병원
        * `CA0007` - 유치원
        * `CA9999` - 기타
        """)
public enum CategoryCode {
    CA0000("CA0000","전체",0,""),
    CA0001("CA0001","산책",10,"CA0001_List"), // 산책
    CA0002("CA0002","카페",10,"CA0002_List"), // 카페
    CA0003("CA0003","식당",10, "CA0003_List"), // 식당
    CA0004("CA0004","목욕",10, "CA0004_List"), // 목욕
    CA0005("CA0005","미용",10, "CA0005_List"), // 미용
    CA0006("CA0006","병원",10, "CA0006_List"), // 병원
    CA0007("CA0007","유치원",10, "CA0007_List"), // 유치원
    CA9999("CA9999","기타",10, ""); // 기타

    private final String code;
    private final String value;
    private final Integer point;
    private final String sheetName;

    CategoryCode(String code, String value, Integer point, String sheetName) {
        this.code = code;
        this.value = value;
        this.point = point;
        this.sheetName = sheetName;
    }

    public static final EnumSet<CategoryCode> MENU_CODES = EnumSet.of(CA0002, CA0003);

    private static final EnumSet<CategoryCode> SKIPPABLE_CODES = EnumSet.of(CA0000, CA9999);

    public boolean shouldSkip() {
        return SKIPPABLE_CODES.contains(this);
    }

    // @RequestParam ENUM Parsing
    @JsonCreator
    public static CategoryCode from(String s) {
        return CategoryCode.valueOf(s);
    }
}