package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.certification.Certification;
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
    private final MungpleService mungpleService;
    private final LikeListService likeListService;
    private final WardOfficeService wardOfficeService;

    public Map<String, Object> getMap(int userId) {
        List<Certification> certifications = certService.getLive(userId);  // 라이브 인증 리스트 조회
        certifications.forEach(c ->c.liked(likeListService.hasLiked(userId, c.getCertificationId()))); // 유저가 좋아요 누른 인증 체크

       return Map.of(
                "mungpleList", mungpleService.getMungpleAll(), // mungpleList :  멍플 리스트
                "wardOffice", wardOfficeService.getWardOfficeByGeoCode(userService.getUserById(userId).getGeoCode()), // wardOfficeList : 구군청 위치
                "certNormalList", certifications.stream().filter(c -> c.getMungpleId() == 0).collect(Collectors.toList()), // certNormalList : 일반 인증 리스트 ( 하얀 테두리 )
                "certMungpleList", certifications.stream().filter(c -> c.getMungpleId() != 0).collect(Collectors.toList())); // certMunpleList : 멍플 인증 리스트 ( 주황 테두리 )
    }

    public List<Mungple> getMungple(CategoryCode categoryCode) {
        return (categoryCode.equals(CategoryCode.TOTAL))
                ? mungpleService.getMungpleAll()
                : mungpleService.getMungpleByCategoryCode(categoryCode.getCode());
    }
}
