package com.delgo.reward.certification.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;


@Getter
@Builder
@AllArgsConstructor
public class CertCondition {
    int userId;
    int mungpleId;
    LocalDate date;
    Boolean isCorrect;
    Pageable pageable;


    public static CertCondition from(Boolean isCorrect, Pageable pageable) {
        return CertCondition.builder()
                .isCorrect(isCorrect)
                .pageable(pageable)
                .build();
    }

    public static CertCondition byUser(int userId, Boolean isCorrect, Pageable pageable) {
        return CertCondition.builder()
                .userId(userId)
                .isCorrect(isCorrect)
                .pageable(pageable)
                .build();
    }

    public static CertCondition byMungple(int mungpleId, Boolean isCorrect, Pageable pageable) {
        return CertCondition.builder()
                .mungpleId(mungpleId)
                .isCorrect(isCorrect)
                .pageable(pageable)
                .build();
    }

    public static CertCondition byDateAndUser(LocalDate date, int userId, Boolean isCorrect, Pageable pageable) {
        return CertCondition.builder()
                .date(date)
                .userId(userId)
                .isCorrect(isCorrect)
                .pageable(pageable)
                .build();
    }
}
