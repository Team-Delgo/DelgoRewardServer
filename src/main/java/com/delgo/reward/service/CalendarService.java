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
        List<CertByAchvResDTO> certifications = certService.getCertListByUserId(userId).stream().map(c -> new CertByAchvResDTO(c, userId)).toList();

        return certifications.stream()
                .sorted(Comparator.comparing(CertByAchvResDTO::getCreatedDate)) // 등록 순으로 정렬
                .map(cert -> {
                    List<CertByAchvResDTO> dateList = certifications.stream()
                            .filter(c -> c.getCreatedDate().isAfter(LocalDate.from(cert.getCreatedDate()).atTime(0, 0, 0).minusSeconds(1)) && c.getCreatedDate().isBefore(LocalDate.from(cert.getCreatedDate()).atTime(0, 0, 0).plusDays(1)))
                            .sorted(Comparator.comparing(CertByAchvResDTO::getCreatedDate).reversed())
                            .collect(Collectors.toList());

                    return new CalendarRecord(
                            LocalDate.from(cert.getCreatedDate()),
                            dateList.stream().anyMatch(CertByAchvResDTO::getIsAchievements),
                            dateList);
                }).distinct().collect(Collectors.toList()); // 중복 제거
    }
}
