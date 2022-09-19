package com.delgo.reward.service;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;

    private final LocalDateTime start = LocalDate.now().atTime(0, 0, 0);
    private final LocalDateTime end = LocalDate.now().atTime(0, 0, 0).plusDays(1);

    // 전체 Certification 리스트 조회
    public List<Certification> getCertificationAll() {
        return certificationRepository.findAll();
    }

    // categoryCode & userId로 Certification 리스트 조회
    public List<Certification> getCertificationByUserIdAndCategoryCode(int userId, String categoryCode) {
        return certificationRepository.findByUserIdAndCategoryCode(userId, categoryCode);
    }

    // userId로 Certification 조회
    public List<Certification> getCertificationByUserId(int userId) {
        return certificationRepository.findByUserId(userId);
    }

    // CertificationId로 Certification 조회
    public Certification getCertificationByCertificationId(int certificationId) {
        return certificationRepository.findByCertificationId(certificationId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Certification"));
    }

    // Certification 등록
    public Certification registerCertification(Certification certification) {
        return certificationRepository.save(certification);
    }

    // 6시간 이내 같은 장소 인증 불가능 ( 멍플만 )
    public boolean checkMungpleCertRegister(int userId, int mungpleId) {
        List<Certification> list = certificationRepository.findByUserIdAndMungpleIdAndRegistDtBetween(userId, mungpleId, start, end);
        List<Certification> sortedList = list.stream().sorted(Comparator.comparing(Certification::getRegistDt).reversed()).collect(Collectors.toList());

        // 최근 등록시간이랑 now 비교
        LocalDateTime recentRegistDt = sortedList.get(0).getRegistDt();
        long diff = ChronoUnit.SECONDS.between(recentRegistDt, LocalDateTime.now());

        // 최근 등록시간이랑 비교시간이 6시간(21600초)이내일 경우 오류 반환
        return diff > 21600;
    }

    // 하루에 같은 카테고리 5번 이상 인증 불가능
    public boolean checkCertRegister(int userId, String categoryCode) {
        List<Certification> list = certificationRepository.findByUserIdAndCategoryCodeAndRegistDtBetween(userId, categoryCode, start, end);
        return list.size() < 5;
    }
}
