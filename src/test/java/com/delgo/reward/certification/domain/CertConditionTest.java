package com.delgo.reward.certification.domain;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;



public class CertConditionTest {
    @Test
    public void from으로_Cert_조회_조건을_만들_수_있다() {
        // given
        Boolean isCorrect = true;
        Pageable pageable = PageRequest.of(0,3);

        // when
        CertCondition condition = CertCondition.from(isCorrect, pageable);

        // then
        assertThat(condition.getIsCorrect()).isEqualTo(isCorrect);
        assertThat(condition.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(condition.getPageable().getPageSize()).isEqualTo(3);

    }
    @Test
    public void byUser로_Cert_조회_조건을_만들_수_있다() {
        // given
        int userId = 2;
        Boolean isCorrect = true;
        Pageable pageable = PageRequest.of(0,3);

        // when
        CertCondition condition = CertCondition.byUser(userId, isCorrect, pageable);

        // then
        assertThat(condition.getUserId()).isEqualTo(userId);
        assertThat(condition.getIsCorrect()).isEqualTo(isCorrect);
        assertThat(condition.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(condition.getPageable().getPageSize()).isEqualTo(3);
    }
    @Test
    public void byMungple로_Cert_조회_조건을_만들_수_있다() {
        // given
        int mungpleId = 20;
        Boolean isCorrect = true;
        Pageable pageable = PageRequest.of(0,3);

        // when
        CertCondition condition = CertCondition.byMungple(mungpleId, isCorrect, pageable);

        // then
        assertThat(condition.getMungpleId()).isEqualTo(mungpleId);
        assertThat(condition.getIsCorrect()).isEqualTo(isCorrect);
        assertThat(condition.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(condition.getPageable().getPageSize()).isEqualTo(3);
    }
    @Test
    public void byDateAndUser로_Cert_조회_조건을_만들_수_있다() {
        // given
        int userId = 2;
        LocalDate date = LocalDate.now();
        Boolean isCorrect = true;
        Pageable pageable = PageRequest.of(0,3);

        // when
        CertCondition condition = CertCondition.byDateAndUser(date, userId, isCorrect, pageable);

        // then
        assertThat(condition.getUserId()).isEqualTo(userId);
        assertThat(condition.getDate()).isEqualTo(date);
        assertThat(condition.getIsCorrect()).isEqualTo(isCorrect);
        assertThat(condition.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(condition.getPageable().getPageSize()).isEqualTo(3);
    }
}
