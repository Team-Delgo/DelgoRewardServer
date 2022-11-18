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
    private final LikeListService likeListService;

    // 전체 Certification 리스트 조회
    public List<CalendarDTO> makeCalendarData(int userId) {

        // User의 모든 인증 데이터 조회
        List<Certification> certificationList = certificationService.getCertificationByUserId(userId);

        // User가 좋아요 누른 Certification Check
        for(Certification certification : certificationList)
            certification.setIsLike((likeListService.hasLiked(userId, certification.getCertificationId()))? 1 : 0);

        // 등록 날짜 및 시간 별 오름차순 정렬 ( 메인 사진 : 해당 날짜에 가장 빨리 등록한 사진 )
        List<Certification> sortedList = certificationList.stream().sorted(Comparator.comparing(Certification::getRegistDt)).collect(Collectors.toList());

        List<CalendarDTO> calendarList = new ArrayList<>();
        for (Certification certification : sortedList) {
            // ex) date = 2022-09-16 -> start = 2022-09-15 23:59:59 , end = 2022-09-16 00:00:00
            LocalDate date = LocalDate.from(certification.getRegistDt());
            LocalDateTime start = date.atTime(0, 0, 0).minusSeconds(1); // 1초 빼주는 이유 -> .isAfter로 인해 정각 인증 건은 체크가 안됨
            LocalDateTime end = date.atTime(0, 0, 0).plusDays(1);

            List<Certification> dateList = sortedList.stream().filter(c -> c.getRegistDt().isAfter(start) && c.getRegistDt().isBefore(end)).collect(Collectors.toList());
            // 업적 성취 여부 확인
            List<Certification> isAchievementsList = dateList.stream().filter(c -> c.getIsAchievements() == 1).collect(Collectors.toList());
            int isAchievements = (isAchievementsList.size() > 0) ? 1 : 0;

            calendarList.add(CalendarDTO.builder()
                    .date(date)
                    .isAchievements(isAchievements)
                    .dateList(dateList)
                    .build()
            );
        }

        // 중복 제거 후 반환
        return calendarList.stream().distinct().collect(Collectors.toList());
    }
}
