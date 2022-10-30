package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.*;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.CertificationDTO;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final UserService userService;
    private final PointService pointService;

    /*
     * 인증 등록
     * Request Data : CertificationDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated @RequestBody CertificationDTO certificationDTO) {

        // 하루에 같은 카테고리 5번 이상 인증 불가능
        if (!certificationService.checkCertRegister(certificationDTO.getUserId(), certificationDTO.getCategoryCode()))
            return ErrorReturn(ApiCode.CERTIFICATION_CATEGPRY_COUNT_ERROR);

        // userId != 0이면 멍플 인증이라 판단
        if (certificationDTO.getMungpleId() != 0) {
            // 6시간 이내 같은 장소 인증 불가능 (멍플만)
            if (!certificationService.checkMungpleCertRegister(certificationDTO.getUserId(), certificationDTO.getMungpleId()))
                return ErrorReturn(ApiCode.CERTIFICATION_TIME_ERROR);

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
        Code code = codeService.getGeoCodeBySIGUGUN(userLocation); // GeoCode

        Certification certification = certificationService.registerCertification(certificationDTO.makeCertification(code));

        // 인증시 획득 가능한 업적 있는지 확인 ( 멍플, 일반 구분 )
        int isMungple = (certificationDTO.getMungpleId() != 0) ? 1 : 0;
        List<Achievements> earnAchievementsList = achievementsService.checkEarnAchievements(certificationDTO.getUserId(), isMungple);

        if (earnAchievementsList.size() > 0) {
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

        // Point 부여
        User user = userService.getUserByUserId(certificationDTO.getUserId());
        CategoryCode category = CategoryCode.valueOf(certificationDTO.getCategoryCode());
        pointService.updateAccumulatedPoint(user.getUserId(), category.getPoint());
        pointService.updateWeeklyPoint(user.getUserId(), category.getPoint());

        userService.updateUserData(user);

        return SuccessReturn(returnCertification);
    }

    /*
     * 인증 카테고리 별 조회
     * Request Data : userId, categoryCode
     * - CA0000 = 전체 조회
     * Response Data : 카테고리별 인증 리스트 반환
     */
    @GetMapping("/category-data")
    public ResponseEntity getCategoryData(
            @RequestParam Integer userId,
            @RequestParam String categoryCode,
            @RequestParam Integer currentPage,
            @RequestParam Integer pageSize) {
        // Validate - Blank Check; [ String 만 해주면 됨 ]
        if (categoryCode.isBlank())
            return ErrorReturn(ApiCode.PARAM_ERROR);

        List<Certification> certificationList = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certificationService.getCertificationByUserIdAndCategoryCode(userId, categoryCode, currentPage, pageSize)
                : certificationService.getCertificationByUserIdPaging(userId, currentPage, pageSize);

        return SuccessReturn(certificationList);
    }

    /*
     * 인증 게시글의 좋아요 + 1
     * Request Data : certificationId
     * - plusLikeCount: JPA 안 쓰고 JDBC_TEMPLATE 사용한 이유
     * : 업데이트 속도 때문에, JPA로 업데이트 할 경우 너무 느려서 놓치는 요청이 발생.
     * Response Data : X
     */
    @PostMapping("/like")
    public ResponseEntity setLike(@RequestParam Integer certificationId) {
        certificationService.plusLikeCount(certificationId);
        return SuccessReturn();
    }

    /*
     * 카테고리 별 인증 개수 반환 ( 유저 )
     * Request Data : userId
     * Response Data : 카테고리별 인증 개수 반환
     */
    @GetMapping("/category-data-count")
    public ResponseEntity getCountData(@RequestParam Integer userId) {
        List<Certification> certificationList = certificationService.getCertificationByUserId(userId);
        Map<String, Integer> returnMap = new HashMap<>();
        for (CategoryCode categoryCode : CategoryCode.values()) {
            int count = (int) certificationList.stream().filter(c -> c.getCategoryCode().equals(categoryCode.getCode())).count();
            returnMap.put(categoryCode.getValue(), count);
        }

        return SuccessReturn(returnMap);
    }

    /*
     * 가장 최근 등록한 인증 반환 [ Main ]
     * Request Data :
     * Response Data : 최근 등록 인증 2개 반환
     */
    @GetMapping("/data/main")
    public ResponseEntity getMainData() {
        return SuccessReturn(certificationService.getRecentCertificationList());
    }

    /*
     * 모든 인증 리스트 페이징으로 반환 [ Main ]
     * Request Data : currentPage ( 현재 페이지 번호 ), pageSize ( 페이지 크기 )
     * Response Data : 인증 모두 조회 ( 페이징 처리 되어 있음 )
     */
    @GetMapping("/data/all")
    public ResponseEntity getPagingData(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        return SuccessReturn(certificationService.getCertificationAll(currentPage, pageSize));
    }
}