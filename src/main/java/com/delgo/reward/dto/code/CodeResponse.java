package com.delgo.reward.dto.code;


import com.delgo.reward.comm.code.CodeType;
import com.delgo.reward.domain.code.Code;
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

    private Integer n_code;
    private Integer n_pCode;

    public static CodeResponse from(Code code) {
        if (code.getType().equals(CodeType.geo))
            return CodeResponse.builder()
                    .code(code.getCode())
                    .pCode(code.getPCode())
                    .codeName(code.getCodeName())
                    .codeDesc(code.getCodeDesc())
                    .n_code(Integer.parseInt(code.getCode()))
                    .n_pCode(Integer.parseInt(code.getPCode()))
                    .build();
        else
            return CodeResponse.builder()
                    .code(code.getCode())
                    .pCode(code.getPCode())
                    .codeName(code.getCodeName())
                    .codeDesc(code.getCodeDesc())
                    .build();
    }

    public static List<CodeResponse> fromList(List<Code> codeList) {
        return codeList.stream().map(CodeResponse::from).toList();
    }
}