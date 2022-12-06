package com.delgo.reward.service;


import com.delgo.reward.domain.Certification;
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

    private final UserService userService;
    private final MungpleService mungpleService;
    private final LikeListService likeListService;
    private final CertService certificationService;
    private final WardOfficeService wardOfficeService;

    public Map getMap(int userId) {
        // 인증 리스트 조회
        List<Certification> certifications = certificationService.getLive(userId);
        certifications.forEach(certification ->certification.setIsLike(likeListService.hasLiked(userId, certification.getCertificationId())));

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("mungpleList", mungpleService.getMungpleAll()); // mungpleList :  멍플 리스트
        resultMap.put("wardOffice", wardOfficeService.getWardOfficeByGeoCode(userService.getUserByUserId(userId).getGeoCode())); // wardOfficeList : 구군청 위치
        resultMap.put("certNormalList", certifications.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()));  // certNormalList : 일반 인증 리스트 ( 하얀 테두리 )
        resultMap.put("certMungpleList", certifications.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList())); // certMunpleList : 멍플 인증 리스트 ( 주황 테두리 )

        return resultMap;
    }
}
