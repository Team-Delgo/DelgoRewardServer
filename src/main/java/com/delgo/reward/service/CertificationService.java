package com.delgo.reward.service;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;

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
}
