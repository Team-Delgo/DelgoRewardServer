package com.delgo.reward.service;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.CalendarDTO;
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
    private final LikeListService likeListService;

    public List<CalendarDTO> getCalendar(int userId) {
        List<Certification> certifications = certService.getCertByUserId(userId);

        return certifications.stream()
                .sorted(Comparator.comparing(Certification::getRegistDt)) // 등록 순으로 정렬
                .map(cert -> {cert.liked(likeListService.hasLiked(userId, cert.getCertificationId())); // 유저가 좋아요 누른 인증 체크
                    List<Certification> dateList = certifications.stream()
                            .filter(c -> c.getRegistDt().isAfter(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).minusSeconds(1)) &&
                                    c.getRegistDt().isBefore(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).plusDays(1)))
                            .sorted(Comparator.comparing(Certification::getRegistDt).reversed())
                            .collect(Collectors.toList());

                    return CalendarDTO.builder()
                            .date(LocalDate.from(cert.getRegistDt()))
                            .isAchievements(dateList.stream().anyMatch(Certification::getIsAchievements))
                            .dateList(dateList)
                            .build();
                }).distinct().collect(Collectors.toList()); // 중복 제거
    }
}
