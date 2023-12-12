package com.delgo.reward.record.calendar;

import com.delgo.reward.dto.cert.CertResponse;

import java.time.LocalDate;
import java.util.List;

public record CalendarRecord(
        LocalDate date,
        List<CertResponse> dateList) {
}
