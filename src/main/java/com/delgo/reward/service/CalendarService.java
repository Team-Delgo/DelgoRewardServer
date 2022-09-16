package com.delgo.reward.service;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.dto.CalendarDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CertificationService certificationService;

    // 전체 Certification 리스트 조회
    public List<CalendarDTO> makeCalendarData(int userId) {
        // User의 모든 인증 데이터 조회
        List<Certification> certificationList = certificationService.getCertificationByUserId(userId);
        // 등록 날짜 및 시간 별 오름차순 정렬 ( 메인 사진 : 해당 날짜에 가장 빨리 등록한 사진 )
        List<Certification> sortedList = certificationList.stream().sorted(Comparator.comparing(Certification::getRegistDt)).collect(Collectors.toList());

        List<CalendarDTO> calendarList = new ArrayList<>();
        for (Certification certification : sortedList) {
            // ex) date = 2022-09-16 -> start = 2022-09-15 23:59:59 , end = 2022-09-16 00:00:00
            LocalDate date = LocalDate.from(certification.getRegistDt());
            LocalDateTime start = date.atTime(0, 0, 0).minusSeconds(1); // 1초 빼주는 이유 -> .isAfter로 인해 정각 인증 건은 체크가 안됨
            LocalDateTime end = date.atTime(0, 0, 0).plusDays(1);

            List<Certification> dateList = sortedList.stream().filter(c -> c.getRegistDt().isAfter(start) && c.getRegistDt().isBefore(end)).collect(Collectors.toList());
            calendarList.add(CalendarDTO.builder()
                    .date(date)
                    .dateList(dateList)
                    .build()
            );
        }

        // 중복 제거 후 반환
        return calendarList.stream().distinct().collect(Collectors.toList());
    }
}
