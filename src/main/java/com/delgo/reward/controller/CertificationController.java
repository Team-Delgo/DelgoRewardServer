package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import com.delgo.reward.domain.Mungple;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.dto.CertificationDTO;
import com.delgo.reward.service.CertificationService;
import com.delgo.reward.service.CodeService;
import com.delgo.reward.service.MungpleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/certification")
public class CertificationController extends CommController {

    private final CertificationService certificationService;
    private final MungpleService mungpleService;
    private final ReverseGeoService reverseGeoService;
    private final CodeService codeService;
    private final GeoService geoService;

    /*
     * 인증 등록
     * Request Data : CertificationDTO
     * - TODO : 멍플일 경우 거리 비교 구현
     * - TODO : 중복인증 막아야 함. ( 멍플은 가능해 보임 , 일반 인증은 ? )
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated @RequestBody CertificationDTO certificationDTO) {
        // GeoCode 조회
        Location userLocation = reverseGeoService.getReverseGeoData(new Location(certificationDTO.getLatitude(), certificationDTO.getLongitude()));
        Code code = codeService.getGeoCodeBySIGUGUN(userLocation.getSIGUGUN()); // GeoCode

        // 멍플 인증
        if (certificationDTO.getMungpleId() != 0)
        {
            Mungple mungple = mungpleService.getMungpleByMungpleId(certificationDTO.getMungpleId());
            // 사용자 실수 방시 ( 멍플 인증일 경우 멍플의 카테고리 넣어준다.
            certificationDTO.setCategoryCode(mungple.getCategoryCode());
            // 멍플 <-> 유저와의 거리
            double distance = geoService.getDistance(mungple.getRoadAddress(), certificationDTO.getLatitude(), certificationDTO.getLongitude());
            if (distance > 30) // 30m 이상 떨어진 곳에서 인증시 인증 불가
                return ErrorReturn(ApiCode.TOO_FAR_DISTANCE);
        }


        Certification certification = certificationService.registerCertification(certificationDTO.makeCertification(code));
        return SuccessReturn(certification);
    }
}