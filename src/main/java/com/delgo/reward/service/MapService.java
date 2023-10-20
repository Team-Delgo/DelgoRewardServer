package com.delgo.reward.service;


import com.delgo.reward.certification.service.CertService;
import com.delgo.reward.comm.code.GeoCode;
import com.delgo.reward.comm.code.PGeoCode;
import com.delgo.reward.certification.domain.Certification;
import com.delgo.reward.certification.controller.res.CertResponse;
import com.delgo.reward.dto.comm.Page;
import com.delgo.reward.dto.map.OtherMapDTO;
import com.delgo.reward.dto.mungple.MungpleResDTO;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.certification.domain.CertCondition;
import com.delgo.reward.certification.service.port.CertRepository;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final CertService certService;
    private final UserService userService;

    private final CertRepository certRepository;
    private final MongoMungpleRepository mongoMungpleRepository;

    public Map<String, Object> getMap(int userId) {
        CertCondition condition = CertCondition.builder()
                .userId(userId)
                .pageable(Pageable.unpaged())
                .build();
        List<CertResponse> certs = certService.getListByCondition(condition).getContent()
                .stream().map(c -> new CertResponse(c,userId)).toList();  // 인증 리스트 조회

        List<MongoMungple > mungples = mongoMungpleRepository.findByIsActive(true);
        List<MungpleResDTO > mungpleResDTOS = mungples.stream().map(MungpleResDTO::new).toList();
        return (userId == 0)
                ? Map.of("mungpleList", mungpleResDTOS)
                : Map.of("mungpleList", mungpleResDTOS,
                "normalCertList", certs.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()),
                "mungpleCertList", certs.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()));
    }

    public OtherMapDTO getOtherMap(int userId) {
        CertCondition condition = CertCondition.builder()
                .userId(userId)
                .isCorrect(true)
                .pageable(Pageable.unpaged())
                .build();
        Page<Certification> page = certService.getListByCondition(condition);
        return new OtherMapDTO(
                userService.getUserById(userId),
                page.getContent().stream().map(c -> new CertResponse(c,userId)).toList(),  // 인증 리스트 조회
                page.getTotalCount());
    }

    /**
     * Map TEST
     */
    public Map<String, List<Certification>> test() {
        return test(6);
    }

    public Map<String, List<Certification>> test(int count){
        Map<String, List<Certification>> certByPGeoCode = new ArrayMap<>();
        List<Certification> certificationList;

        for(PGeoCode p: PGeoCode.values()){
            if(p.getPGeoCode().equals(PGeoCode.P101000.getPGeoCode())){
                List<Certification> certificationListOfSongPa = certRepository.findCertByGeoCode(GeoCode.C101180.getGeoCode(), PageRequest.of(0, 3));
                List<Certification> certificationListNotOfSongPa = certRepository.findCertByPGeoCodeExceptGeoCode(p.getPGeoCode(), GeoCode.C101180.getGeoCode(), PageRequest.of(0, 3));

                certificationList = Stream.concat(certificationListOfSongPa.stream(), certificationListNotOfSongPa.stream())
                        .collect(Collectors.toList());
            } else {
                certificationList = certRepository.findCertByPGeoCode(p.getPGeoCode(), PageRequest.of(0, count));
            }
            if(certificationList.size() > 0){
                certByPGeoCode.put(p.getPGeoCode(), certificationList);
            }
        }

        return certByPGeoCode;
    }
}
