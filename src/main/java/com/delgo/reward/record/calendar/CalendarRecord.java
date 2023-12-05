package com.delgo.reward.record.calendar;

import com.delgo.reward.dto.cert.CertResDTO;

import java.time.LocalDate;
import java.util.List;

public record CalendarRecord(
        LocalDate date,
        List<CertResDTO> dateList) {
}
