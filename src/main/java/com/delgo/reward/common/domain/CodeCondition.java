package com.delgo.reward.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CodeCondition {
    private String code; // 코드
    private String pCode; // 부모코드
    private String codeName; // 코드명

    public static CodeCondition byCode(String code) {
        return CodeCondition.builder()
                .code(code)
                .build();
    }

    public static CodeCondition byCodeName(String codeName) {
        return CodeCondition.builder()
                .codeName(codeName)
                .build();
    }

    public static CodeCondition byPCodeAndCodeName(String pCode, String codeName) {
        return CodeCondition.builder()
                .pCode(pCode)
                .codeName(codeName)
                .build();
    }
}
