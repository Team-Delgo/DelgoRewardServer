package com.delgo.reward.common.domain;


import lombok.*;


@Getter
@Builder
@AllArgsConstructor
public class Code {
    private String code; // 코드
    private String pCode; // 부모코드
    private String codeName; // 코드명
    private String codeDesc; // 코드 설명
    private CodeType type; // 코드 타입 (Ex. geo, dog )
}