package com.delgo.reward.domain.code;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    @Id
    private String code; // 코드
    private String pCode; // 부모코드
    private String codeName; // 코드명
    private String codeDesc; // 코드 설명
    private String type; // 코드 타입 (Ex. geo, dog )

    @Transient
    private Integer n_code;
    @Transient
    private Integer n_pCode;

    @CreationTimestamp
    private LocalDateTime registDt;

    public Code formatInteger(){
        this.n_code = Integer.parseInt(code);
        this.n_pCode = Integer.parseInt(pCode);

        return this;
    }
}