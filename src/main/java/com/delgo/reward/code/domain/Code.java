package com.delgo.reward.code.domain;


import com.delgo.reward.comm.code.CodeType;
import com.delgo.reward.domain.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code extends BaseTimeEntity {
    @Id
    private String code; // 코드
    private String pCode; // 부모코드
    private String codeName; // 코드명
    private String codeDesc; // 코드 설명
    @Enumerated(EnumType.STRING)
    private CodeType type; // 코드 타입 (Ex. geo, dog )
}