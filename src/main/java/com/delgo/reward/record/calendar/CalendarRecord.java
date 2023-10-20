package com.delgo.reward.record.calendar;

import com.delgo.reward.certification.controller.res.CertResponse;

import java.time.LocalDate;
import java.util.List;

public record CalendarRecord(
        LocalDate date,
        List<CertResponse> dateList) {
}
