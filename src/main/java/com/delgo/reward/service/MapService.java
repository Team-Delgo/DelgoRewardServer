package com.delgo.reward.service;


import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.dto.cert.CertByMungpleResDTO;
import com.delgo.reward.dto.map.OtherMapDTO;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
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
    private final UserService userService;
    private final MongoMungpleRepository mongoMungpleRepository;

    public Map<String, Object> getMap(int userId) {
        List<CertByMungpleResDTO> certs = certService.getCertsByUserId(userId).stream().map(c -> new CertByMungpleResDTO(c,userId)).toList();  // 인증 리스트 조회
        List<CertByMungpleResDTO> exposedCertList = certService.getExposedCerts(3).stream().map(c -> new CertByMungpleResDTO(c,userId)).toList();  // 노출시킬 인증 리스트 조회

        List<MongoMungple > mungples = mongoMungpleRepository.findByIsActive(true);
        List<MungpleResDTO > mungpleResDTOS = mungples.stream().map(MungpleResDTO::new).toList();
        return (userId == 0)
                ? Map.of("mungpleList", mungpleResDTOS,
                "exposedNormalCertList", exposedCertList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()),
                "exposedMungpleCertList", exposedCertList.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()))
                : Map.of("mungpleList", mungpleResDTOS,
                "normalCertList", certs.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()),
                "mungpleCertList", certs.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()),
                "exposedNormalCertList", exposedCertList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()),
                "exposedMungpleCertList", exposedCertList.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()));
    }

    public OtherMapDTO getOtherMap(int userId) {
        return new OtherMapDTO(
                userService.getUserById(userId),
                certService.getCorrectCertsByUserId(userId).stream().map(c -> new CertByMungpleResDTO(c,userId)).toList(),  // 인증 리스트 조회
                certService.getCorrectCertCountByUserId(userId)
        );
    }

    public Map<String, List<Certification>> test() {
        return certService.test(6);
    }
}
