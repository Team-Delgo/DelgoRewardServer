package com.delgo.reward.record.calendar;

import com.delgo.reward.domain.certification.Certification;

import java.time.LocalDate;
import java.util.List;

public record CalendarRecord(
        LocalDate date,
        Boolean isAchievements,
        List<Certification> dateList) {
}
