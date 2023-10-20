package com.delgo.reward.repository.certification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


@Getter
@Builder
@ToString
@AllArgsConstructor
public class CertCondition {
    int userId;
    int mungpleId;
    LocalDate date;
    Boolean isCorrect;
    Pageable pageable;
}
