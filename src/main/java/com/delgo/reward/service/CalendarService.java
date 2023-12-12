package com.delgo.reward.service;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.certification.Reaction;
import com.delgo.reward.dto.cert.CertResponse;
import com.delgo.reward.record.calendar.CalendarRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {
    private final CertService certService;
    private final ReactionService reactionService;

    public List<CalendarRecord> getCalendar(int userId) {
        List<Certification> certificationList = certService.getCertsByUserId(userId);
        Map<Integer,List<Reaction>> reactionMap = reactionService.getMapByCertList(certificationList);
        List<CertResponse> certifications = CertResponse.fromList(userId, certificationList, reactionMap);

        return certifications.stream()
                .sorted(Comparator.comparing(CertResponse::getRegistDt)) // 등록 순으로 정렬
                .map(cert -> {
                    List<CertResponse> dateList = certifications.stream()
                            .filter(c -> c.getRegistDt().isAfter(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).minusSeconds(1)) && c.getRegistDt().isBefore(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).plusDays(1)))
                            .sorted(Comparator.comparing(CertResponse::getRegistDt).reversed())
                            .collect(Collectors.toList());

                    return new CalendarRecord(
                            LocalDate.from(cert.getRegistDt()),
                            dateList);
                }).distinct().collect(Collectors.toList()); // 중복 제거
    }
}
