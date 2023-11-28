package com.delgo.reward.common.infrastructure.entity;


import com.delgo.reward.common.domain.Code;
import com.delgo.reward.common.domain.CodeType;
import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "code")
public class CodeEntity extends BaseTimeEntity {
    @Id
    private String code; // 코드
    private String pCode; // 부모코드
    private String codeName; // 코드명
    private String codeDesc; // 코드 설명

    @Enumerated(EnumType.STRING)
    private CodeType type; // 코드 타입 (Ex. geo, dog )

    public Code toModel() {
        return Code.builder()
                .code(code)
                .pCode(pCode)
                .codeName(codeName)
                .codeDesc(codeDesc)
                .build();
    }

    public static CodeEntity from(Code code) {
        return CodeEntity.builder()
                .code(code.getCode())
                .pCode(code.getPCode())
                .codeName(code.getCodeName())
                .codeDesc(code.getCodeDesc())
                .build();
    }
}