package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.*;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.certification.LiveCertDTO;
import com.delgo.reward.dto.certification.PastCertDTO;
import com.delgo.reward.dto.certification.ModifyCertDTO;
import com.delgo.reward.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
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
public class CertController extends CommController {

    private final GeoService geoService;
    private final CodeService codeService;
    private final UserService userService;
    private final PointService pointService;
    private final PhotoService photoService;
    private final RankingService rankingService;
    private final ArchiveService archiveService;
    private final MungpleService mungpleService;
    private final LikeListService likeListService;
    private final CertService certificationService;
    private final ReverseGeoService reverseGeoService;
    private final AchievementsService achievementsService;

    /*
     * 인증 등록 [Live]
     * Request Data : LiveCertDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/live")
    public ResponseEntity registerLive(@Validated @RequestBody LiveCertDTO dto) {
        // 하루에 같은 카테고리 5번 이상 인증 불가능
        if (!certificationService.checkCertRegister(dto.getUserId(), dto.getCategoryCode(), true))
            return ErrorReturn(ApiCode.CERTIFICATION_CATEGPRY_COUNT_ERROR);

        boolean isMungple = dto.getMungpleId() != 0;
        if (isMungple) {
            // 6시간 이내 같은 장소 인증 불가능 (멍플만)
            if (!certificationService.checkMungpleCertRegister(dto.getUserId(), dto.getMungpleId(), true))
                return ErrorReturn(ApiCode.CERTIFICATION_TIME_ERROR);

            Mungple mungple = mungpleService.getMungpleByMungpleId(dto.getMungpleId());

            // 사용자 실수 방지
            dto.setCategoryCode(mungple.getCategoryCode());
            dto.setPlaceName(mungple.getPlaceName());

            // 멍플 <-> 유저와의 거리
            double distance = geoService.getDistance(mungple.getRoadAddress(), dto.getLongitude(), dto.getLatitude());
            if (distance > 100) // 100m 이상 떨어진 곳에서 인증시 인증 불가
                return ErrorReturn(ApiCode.TOO_FAR_DISTANCE);
        }

        // GeoCode 조회
        Location location = reverseGeoService.getReverseGeoData(new Location(dto.getLatitude(), dto.getLongitude()));
        Code code = codeService.getGeoCodeBySIGUGUN(location); // GeoCode
        String address = location.getSIDO() + " " + location.getSIGUGUN(); // address

        Certification certification = certificationService.registerCertification(dto.toEntity(code, address));

        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(), isMungple);
        if (earnAchievements.size() > 0) {
            for (Achievements achievements : earnAchievements) {
                Archive archive = Archive.builder()
                        .achievementsId(achievements.getAchievementsId())
                        .userId(dto.getUserId())
                        .isMain(0)
                        .build();
                archiveService.registerArchive(archive);
            }


            // 해당 인증이 업적에 영향을 주었는지 체크
            certification.setIsAchievements(true);
            certification.setAchievements(earnAchievements);
        }

        // 사진 파일 저장 추가
        String photoUrl = photoService.uploadCertIncodingFile(certification.getCertificationId(), dto.getPhoto());
        certification.setPhotoUrl(photoUrl);
        dto.setPhoto(""); // log 출력 전 photo 삭제

        Certification returnCertification = certificationService.registerCertification(certification);

        // Point 부여
        User user = userService.getUserByUserId(dto.getUserId());
        CategoryCode category = CategoryCode.valueOf(dto.getCategoryCode());
        pointService.updateAccumulatedPoint(user.getUserId(), category.getPoint());
        pointService.updateWeeklyPoint(user.getUserId(), category.getPoint());

        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();

        log.info("requestBody : {}", dto);
        return SuccessReturn(returnCertification);
    }

    /*
     * 인증 등록 [Past]
     * Request Data : PastCertificationDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/past")
    public ResponseEntity registerPast(@Validated @RequestBody PastCertDTO dto) {
        boolean isMungple = (dto.getMungpleId() != 0);

        Certification certification;
        if (isMungple) {
            Mungple mungple = mungpleService.getMungpleByMungpleId(dto.getMungpleId());
            certification = certificationService.registerCertification(dto.toEntity(mungple));
        }
        else {
            certification = certificationService.registerCertification(dto.toEntity());
        }

        // 인증시 획득 가능한 업적 있는지 확인 ( 멍플, 일반 구분 )
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(), isMungple);

        if (earnAchievements.size() > 0) {
            for (Achievements achievements : earnAchievements) {
                Archive archive = Archive.builder()
                        .achievementsId(achievements.getAchievementsId())
                        .userId(dto.getUserId())
                        .isMain(0)
                        .build();
                archiveService.registerArchive(archive);
            }

            // 해당 인증이 업적에 영향을 주었는지 체크
            certification.setIsAchievements(true);
            certification.setAchievements(earnAchievements);
        }

        Certification returnCertification = certificationService.registerCertification(certification);
        return SuccessReturn(returnCertification);
    }

    /*
     * 인증 수정
     * Request Data : CertificationModifyDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PutMapping
    public ResponseEntity modify(@Validated @RequestBody ModifyCertDTO dto) {
        Certification certification = certificationService.getCertificationByCertificationId(dto.getCertId());

        if(certification.getUserId() != dto.getUserId())
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);

        certification.setDescription(dto.getDescription());
        return SuccessReturn(certificationService.modifyCertification(certification));
    }

    /*
     * 인증 카테고리 별 조회
     * Request Data : userId, categoryCode
     * - CA0000 = 전체 조회
     * Response Data : 카테고리별 인증 리스트 반환
     */

    @GetMapping("/category")
    public ResponseEntity getCategoryData(
            @RequestParam Integer userId,
            @RequestParam String categoryCode,
            @RequestParam Integer currentPage,
            @RequestParam Integer pageSize,
            @RequestParam Boolean isDesc) {
        // Validate - Blank Check; [ String 만 해주면 됨 ]
        if (categoryCode.isBlank())
            return ErrorReturn(ApiCode.PARAM_ERROR);

        Slice<Certification> returnObject = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certificationService.getCertificationByUserIdAndCategoryCode(userId, categoryCode, currentPage, pageSize, isDesc)
                : certificationService.getCertificationByUserIdPaging(userId, currentPage, pageSize, isDesc);

        return SuccessReturn(returnObject);
    }

    /*
     * 카테고리 별 인증 개수 반환 ( 유저 )
     * Request Data : userId
     * Response Data : 카테고리별 인증 개수 반환
     */
    @GetMapping(value = {"/category/count/{userId}","/category/count/"})
    public ResponseEntity getCountData(@PathVariable Integer userId) {
        List<Certification> certificationList = certificationService.getCertificationByUserId(userId);
        Map<String, Integer> returnMap = new HashMap<>();
        for (CategoryCode categoryCode : CategoryCode.values()) {
            int count = (int) certificationList.stream().filter(c -> c.getCategoryCode().equals(categoryCode.getCode())).count();
            returnMap.put(categoryCode.getValue(), count);
        }

        return SuccessReturn(returnMap);
    }

    /*
     * 인증 게시글의 좋아요 + 1
     * Request Data : userId, certificationId
     * - plusLikeCount: JPA 안 쓰고 JDBC_TEMPLATE 사용한 이유
     * : 업데이트 속도 때문에, JPA로 업데이트 할 경우 너무 느려서 놓치는 요청이 발생.
     * Response Data : X
     */
    @PostMapping(value = {"/like/{userId}/{certificationId}","/like/"})
    public ResponseEntity setLike(@PathVariable Integer userId, @PathVariable Integer certificationId) {

        // USER , Certification 존재 여부 체크
        userService.getUserByUserId(userId);
        Certification certification = certificationService.getCertificationByCertificationId(certificationId);

        // 사용자가 해당 Certification 좋아요 눌렀는지 체크.
        if (likeListService.hasLiked(userId, certificationId)) { // 좋아요 존재
            likeListService.delete(userId, certificationId);
            if (certification.getLikeCount() > 0) // 혹시 like_count가 0이거나 혹은 음수이면 miuns 시키지 않는다.
                certificationService.minusLikeCount(certificationId);
        } else {
            likeListService.register(userId, certificationId);
            certificationService.plusLikeCount(certificationId);
        }

        return SuccessReturn();
    }

    /*
     * 가장 최근 등록한 인증 반환 [ Main ]
     * Request Data :
     * Response Data : 최근 등록 인증 2개 반환
     */
    @GetMapping("/main")
    public ResponseEntity getMainData(@RequestParam Integer userId) {
        return SuccessReturn(certificationService.getRecentCertificationList(userId));
    }

    /*
     * 모든 인증 리스트 페이징으로 반환 [ Main ]
     * Request Data : currentPage ( 현재 페이지 번호 ), pageSize ( 페이지 크기 )
     * Response Data : 인증 모두 조회 ( 페이징 처리 되어 있음 )
     */
    @GetMapping("/all")
    public ResponseEntity getPagingData(@RequestParam Integer userId, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        return SuccessReturn(certificationService.getCertificationAll(userId, currentPage, pageSize, true));
    }

    /*
     * 인증 삭제
     * Request Data : userId ( 삭제 요청 userId ), certificationId ( 삭제할 인증 )
     * 요청 userId랑 등록 userId랑 비교 해야 함.
     * Response Data : null
     */
    @DeleteMapping(value={"/{userId}/{certificationId}"})
    public ResponseEntity delete(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        Certification certification = certificationService.getCertificationByCertificationId(certificationId);

        if(userId != certification.getUserId())
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);

        certificationService.delete(certification);
        return SuccessReturn();
    }
}