package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.dto.CertificationDTO;
import com.delgo.reward.service.CertificationService;
import com.delgo.reward.service.CodeService;
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
    private final ReverseGeoService reverseGeoService;
    private final CodeService codeService;

    /*
     * 인증 등록
     * Request Data : CertificationDTO
     * - TODO : 사진 등록 구현
     * - TODO : 멍플일 경우 거리 비교 구현
     * - TODO : 중복인증 막아야 함. ( 멍플은 가능해 보임 , 일반 인증은 ? )
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated @RequestBody CertificationDTO certificationDTO) {

        String photoUrl = "";

        // GeoCode 조회
        Location location = reverseGeoService.getReverseGeoData(new Location(certificationDTO.getLatitude(), certificationDTO.getLongitude()));
        Code code = codeService.getGeoCodeBySIGUGUN(location.getSIGUGUN()); // GeoCode

        Certification certification = certificationService.registerCertification(certificationDTO.makeCertification(code, photoUrl));
        return SuccessReturn(certification);
    }
}