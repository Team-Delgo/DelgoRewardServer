package com.delgo.reward.service;


import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Mungple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MapService {

    private final MungpleService mungpleService;
    private final WardOfficeService wardOfficeService;
    private final CertificationService certificationService;

    public Map getMapData(int userId) {
        // 멍플 조회
        List<Mungple> mungpleList = mungpleService.getMungpleAll();

        // 인증 리스트 조회
        List<Certification> certificationList = certificationService.getCertificationByUserId(userId);

        // 일반 인증, 멍플 인증 구분
        List<Certification> certNormalList = certificationList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList());
        List<Certification> certMunpleList = certificationList.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList());

        // 멍플 인증 된 장소 멍플에서 제거
        for (Certification certification : certMunpleList) {
            mungpleList.removeIf(m -> m.getMungpleId() == certification.getMungpleId());
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("mungpleList", mungpleList); // mungpleList :  멍플 리스트 ( 인증된 멍플은 제거된 리스트 )
        resultMap.put("wardOfficeList", wardOfficeService.getWardOfficeAll()); // wardOfficeList : 구군청 위치
        resultMap.put("certNormalList", certNormalList);  // certNormalList : 일반 인증 리스트 ( 하얀 테두리 )
        resultMap.put("certMungpleList", certMunpleList); // certMunpleList : 멍플 인증 리스트 ( 주황 테두리 )

        return resultMap;
    }

}
