package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.certification.Certification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

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
    private final MungpleService mungpleService;
    private final LikeListService likeListService;
    private final WardOfficeService wardOfficeService;

    public Map<String, Object> getMap(int userId) {
        List<Certification> certifications = certService.getCertByUserId(userId);  // 인증 리스트 조회
        certifications.forEach(c -> c.liked(likeListService.hasLiked(userId, c.getCertificationId()))); // 유저가 좋아요 누른

        List<Certification> exposedCertList = certService.getExposedCert(3);  // 노출시킬 인증 리스트 조회
        exposedCertList.forEach(c -> c.liked(likeListService.hasLiked(userId, c.getCertificationId())));

        return Map.of(
                "mungpleList", mungpleService.getMungpleAll(), // 멍플 리스트
                "wardOffice", wardOfficeService.getWardOfficeByGeoCode(userService.getUserById(userId).getGeoCode()),// 구군청 위치
                "normalCertList", certifications.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()), // 일반 인증 리스트
                "mungpleCertList", certifications.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList()), // 멍플 인증 리스트
                "exposedNormalCertList", exposedCertList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()), // 사용자들에게 노출시킬 인증 리스트
                "exposedMungpleCertList", exposedCertList.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList())); // 사용자들에게 노출시킬 인증 리스트
    }

    public MultiValueMap<String, List<Certification>> test(int userId){

        return certService.test(6);
    }

    public List<Mungple> getMungple(CategoryCode categoryCode) {
        return (categoryCode.equals(CategoryCode.TOTAL))
                ? mungpleService.getMungpleAll()
                : mungpleService.getMungpleByCategoryCode(categoryCode.getCode());
    }
}
