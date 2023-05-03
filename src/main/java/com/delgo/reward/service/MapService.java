package com.delgo.reward.service;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.cert.CertByMungpleResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final CertService certService;
    private final MungpleService mungpleService;

    public Map<String, Object> getMap(int userId) {
        List<CertByMungpleResDTO> certifications = certService.getCertListByUserId(userId).stream().map(c -> new CertByMungpleResDTO(c,userId)).toList();  // 인증 리스트 조회
        List<CertByMungpleResDTO> exposedCertList = certService.getExposedCertList(3).stream().map(c -> new CertByMungpleResDTO(c,userId)).toList();  // 노출시킬 인증 리스트 조회

        return (userId == 0)
                ? Map.of("mungpleList", mungpleService.getMungpleByMap(),
                "exposedNormalCertList", exposedCertList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()),
                "exposedMungpleCertList", exposedCertList.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()))
                : Map.of("mungpleList", mungpleService.getMungpleByMap(),
                "normalCertList", certifications.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()),
                "mungpleCertList", certifications.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()),
                "exposedNormalCertList", exposedCertList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()),
                "exposedMungpleCertList", exposedCertList.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()));
    }

    public Map<String, List<Certification>> test() {
        return certService.test(6);
    }
}
