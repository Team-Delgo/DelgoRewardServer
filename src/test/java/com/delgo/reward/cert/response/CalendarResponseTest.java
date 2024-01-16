package com.delgo.reward.cert.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CalendarResponseTest {

    @Test
    void from() {
        // given
        LocalDateTime date1 = LocalDateTime.of(2024, 1, 1, 4, 7, 0);
        LocalDateTime date2 = LocalDateTime.of(2024, 1, 2, 0, 0, 0);
        CertResponse certResponse1 = CertResponse.builder().certificationId(1).registDt(date1).build();
        CertResponse certResponse2 = CertResponse.builder().certificationId(2).registDt(date2).build();
        List<CertResponse> certResponseList = List.of(certResponse1, certResponse2);

        // when
        List<CalendarResponse> calendarResponseList = CalendarResponse.from(certResponseList);

        // then
        assertThat(calendarResponseList.get(0).getDate()).isEqualTo(date1.toLocalDate());
        assertThat(calendarResponseList.get(0).getDateList().get(0)).isEqualTo(certResponse1);
        assertThat(calendarResponseList.get(1).getDate()).isEqualTo(date2.toLocalDate());
        assertThat(calendarResponseList.get(1).getDateList().get(0)).isEqualTo(certResponse2);
    }
}