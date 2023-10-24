package com.delgo.reward.service;


import com.delgo.reward.certification.controller.port.CertPhotoService;
import com.delgo.reward.certification.controller.port.CertService;
import com.delgo.reward.certification.controller.port.ReactionService;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.domain.CertPhoto;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.domain.Reaction;
import com.delgo.reward.certification.controller.response.CertResponse;
import com.delgo.reward.dto.comm.PageCustom;
import com.delgo.reward.record.calendar.CalendarRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CertService certService;
    private final ReactionService reactionService;
    private final CertPhotoService certPhotoService;

    public List<CalendarRecord> getCalendar(int userId) {
        PageCustom<Certification> page = certService.getListByCondition(CertCondition.byUser(userId, null, Pageable.unpaged()));
        Map<Integer, List<CertPhoto>> photoMap = certPhotoService.getMapByCertList(page.getContent());
        Map<Integer, List<Reaction>> reactionMap = reactionService.getMapByCertList(page.getContent());

        List<CertResponse> certList = page.getContent().stream().map(cert -> {
            List<CertPhoto> photoList = photoMap.get(cert.getCertificationId());
            List<Reaction> reactionList = reactionMap.get(cert.getCertificationId());

            return CertResponse.from(userId, cert, photoList, reactionList);
        }).toList();

        return certList.stream()
                .sorted(Comparator.comparing(CertResponse::getRegistDt)) // 등록 순으로 정렬
                .map(cert -> {
                    List<CertResponse> dateList = certList.stream()
                            .filter(c -> c.getRegistDt().isAfter(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).minusSeconds(1)) && c.getRegistDt().isBefore(LocalDate.from(cert.getRegistDt()).atTime(0, 0, 0).plusDays(1)))
                            .sorted(Comparator.comparing(CertResponse::getRegistDt).reversed())
                            .collect(Collectors.toList());

                    return new CalendarRecord(LocalDate.from(cert.getRegistDt()), dateList);
                }).distinct().collect(Collectors.toList()); // 중복 제거
    }
}
