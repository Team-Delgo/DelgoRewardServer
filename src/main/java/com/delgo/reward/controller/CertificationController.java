package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.*;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.dto.CertificationDTO;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/certification")
public class CertificationController extends CommController {

    private final CertificationService certificationService;
    private final AchievementsService achievementsService;
    private final ArchiveService archiveService;
    private final MungpleService mungpleService;
    private final ReverseGeoService reverseGeoService;
    private final CodeService codeService;
    private final PhotoService photoService;
    private final GeoService geoService;

    /*
     * 인증 등록
     * Request Data : CertificationDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated @RequestBody CertificationDTO certificationDTO) {


        // 하루에 같은 카테고리 5번 이상 인증 불가능
        if (!certificationService.checkCertRegister(certificationDTO.getUserId(), certificationDTO.getCategoryCode()))
            return ErrorReturn(ApiCode.PARAM_ERROR); // TODO: 에러 코드 생성 필요

        // userId != 0이면 멍플 인증이라 판단
        if (certificationDTO.getMungpleId() != 0) {
            // 6시간 이내 같은 장소 인증 불가능 (멍플만)
            if (!certificationService.checkMungpleCertRegister(certificationDTO.getUserId(), certificationDTO.getMungpleId()))
                return ErrorReturn(ApiCode.PARAM_ERROR); // TODO: 에러 코드 생성 필요

            Mungple mungple = mungpleService.getMungpleByMungpleId(certificationDTO.getMungpleId());

            // 사용자 실수 방지
            certificationDTO.setCategoryCode(mungple.getCategoryCode());
            certificationDTO.setPlaceName(mungple.getPlaceName());

            // 멍플 <-> 유저와의 거리
            double distance = geoService.getDistance(mungple.getRoadAddress(), certificationDTO.getLongitude(), certificationDTO.getLatitude());
            if (distance > 30) // 30m 이상 떨어진 곳에서 인증시 인증 불가
                return ErrorReturn(ApiCode.TOO_FAR_DISTANCE);
        }

        // GeoCode 조회
        Location userLocation = reverseGeoService.getReverseGeoData(new Location(certificationDTO.getLatitude(), certificationDTO.getLongitude()));
        Code code = codeService.getGeoCodeBySIGUGUN(userLocation.getSIGUGUN()); // GeoCode

        Certification certification = certificationService.registerCertification(certificationDTO.makeCertification(code));

        // 인증시 획득 가능한 업적 있는지 확인 ( 멍플, 일반 구분 )
        int isMungple = (certificationDTO.getMungpleId() != 0) ? 1 : 0;
        List<Achievements> earnAchievementsList = achievementsService.checkEarnAchievements(certificationDTO.getUserId(), isMungple);

        if(earnAchievementsList.size() > 0)
        {
            for (Achievements achievements : earnAchievementsList) {
                Archive archive = Archive.builder()
                        .achievementsId(achievements.getAchievementsId())
                        .userId(certificationDTO.getUserId())
                        .build();
                archiveService.registerArchive(archive);
            }

            // 해당 인증이 업적에 영향을 주었는지 체크
            certification.setIsAchievements(1);
        }

        // 사진 파일 저장 추가
        String photoUrl = photoService.uploadCertIncodingFile(certification.getCertificationId(), certificationDTO.getPhoto());
        certification.setPhotoUrl(photoUrl);

        Certification returnCertification = certificationService.registerCertification(certification);
       return SuccessReturn(returnCertification);
    }

    /*
     * 인증 카테고리 별 조회
     * Request Data : userId, categoryCode
     * - CA0000 = 전체 조회
     * Response Data : 카테고리별 인증 리스트 반환
     */
    @GetMapping("/getData")
    public ResponseEntity getData(@RequestParam Integer userId, @RequestParam String categoryCode) {
        // Validate - Blank Check; [ String 만 해주면 됨 ]
        if (categoryCode.isBlank())
            return ErrorReturn(ApiCode.PARAM_ERROR);

        List<Certification> certificationList = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certificationService.getCertificationByUserIdAndCategoryCode(userId, categoryCode)
                : certificationService.getCertificationByUserId(userId);

        return SuccessReturn(certificationList);
    }
}