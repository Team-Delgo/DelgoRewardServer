package com.delgo.reward.common.controller.response;


import com.delgo.reward.common.domain.Code;
import com.delgo.reward.common.domain.CodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
public class CodeResponse {
    private String code; // 코드
    private String pCode; // 부모코드
    private String codeName; // 코드명
    private String codeDesc; // 코드 설명
    private CodeType type; // 코드 타입 (Ex. geo, dog )

    private Integer n_code;
    private Integer n_pCode;

    public static CodeResponse from(Code code) {
        return CodeResponse.builder()
                .code(code.getCode())
                .pCode(code.getPCode())
                .codeName(code.getCodeName())
                .codeDesc(code.getCodeDesc())
                .n_code(Integer.parseInt(code.getCode()))
                .n_pCode(Integer.parseInt(code.getPCode()))
                .build();
    }

    public static List<CodeResponse> fromList(List<Code> codeList) {
        return codeList.stream().map(CodeResponse::from).toList();
    }
}