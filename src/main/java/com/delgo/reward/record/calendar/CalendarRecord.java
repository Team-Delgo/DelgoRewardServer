package com.delgo.reward.record.calendar;

import com.delgo.reward.dto.cert.CertByAchvResDTO;

import java.time.LocalDate;
import java.util.List;

public record CalendarRecord(
        LocalDate date,
        Boolean isAchievements,
        List<CertByAchvResDTO> dateList) {
}
