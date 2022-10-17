package com.delgo.reward.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Code {
    @Id
    private String code; // 코드
    private String pCode; // 부모코드
    private String codeName; // 코드명
    private String codeDesc; // 코드 설명

    @Transient
    private Integer n_code;
    @Transient
    private Integer n_pCode;

    @CreationTimestamp
    private LocalDateTime registDt;
}