package com.delgo.reward.service;


import com.delgo.reward.dto.cert.CertByAchvResDTO;
import com.delgo.reward.record.calendar.CalendarRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CertService certService;

    public List<CalendarRecord> getCalendar(int userId) {
        List<CertByAchvResDTO> certifications = certService.getCertsByUserId(userId).stream().map(c -> new CertByAchvResDTO(c, userId)).toList();

        return certifications.stream()
                .sorted(Comparator.comparing(CertByAchvResDTO::getRegistDt)) // 등록 순으로 정렬
                .map(cert -> {
                    List<CertByAchvResDTO> dateList = certifications.stream()
                            .filter(c -> c.getRegistDt().isAfter(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).minusSeconds(1)) && c.getRegistDt().isBefore(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).plusDays(1)))
                            .sorted(Comparator.comparing(CertByAchvResDTO::getRegistDt).reversed())
                            .collect(Collectors.toList());

                    return new CalendarRecord(
                            LocalDate.from(cert.getRegistDt()),
                            dateList.stream().anyMatch(CertByAchvResDTO::getIsAchievements),
                            dateList);
                }).distinct().collect(Collectors.toList()); // 중복 제거
    }
}
