package com.delgo.reward.controller;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.exception.ApiCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.Code;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.achievements.Archive;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.certification.LiveCertDTO;
import com.delgo.reward.dto.certification.ModifyCertDTO;
import com.delgo.reward.dto.certification.PastCertDTO;
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
    private final CertService certService;
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
        if (!certService.checkCertRegister(dto.getUserId(), dto.getCategoryCode(), true))
            return ErrorReturn(ApiCode.CERTIFICATION_CATEGPRY_COUNT_ERROR);

        boolean isMungple = dto.getMungpleId() != 0;
        if (isMungple) {
            // 6시간 이내 같은 장소 인증 불가능 (멍플만)
            if (!certService.checkMungpleCertRegister(dto.getUserId(), dto.getMungpleId(), true))
                return ErrorReturn(ApiCode.CERTIFICATION_TIME_ERROR);

            // 멍플 <-> 유저와의 거리
            double distance = geoService.getDistance(mungpleService.getMungpleById(dto.getMungpleId()).getRoadAddress(), dto.getLongitude(), dto.getLatitude());
            if (distance > 100) // 100m 이상 떨어진 곳에서 인증시 인증 불가
                return ErrorReturn(ApiCode.TOO_FAR_DISTANCE);
        }

        // GeoCode 조회
        Location location = reverseGeoService.getReverseGeoData(new Location(dto.getLatitude(), dto.getLongitude()));
        Code code = codeService.getGeoCodeBySIGUGUN(location); // GeoCode
        String address = location.getSIDO() + " " + location.getSIGUGUN(); // address

        Certification certification = certService.register(dto.toEntity(code, address));
        // 사진 파일 저장 추가
        photoService.uploadCertEncodingFile(certification.getCertificationId(), dto.getPhoto());
        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(), isMungple);
        if (!earnAchievements.isEmpty()) {
            earnAchievements.forEach(achievement -> {
                Archive archive = Archive.builder()
                        .achievementsId(achievement.getAchievementsId())
                        .userId(dto.getUserId())
                        .isMain(0)
                        .build();
                archiveService.register(archive);
            });

            certification.setIsAchievements(true);
            certification.setAchievements(earnAchievements);
            certification = certService.register(certification);
        }

        // Point 부여
        User user = userService.getUserByUserId(dto.getUserId());
        CategoryCode category = CategoryCode.valueOf(dto.getCategoryCode());
        pointService.updateAccumulatedPoint(user.getUserId(), category.getPoint());
        pointService.updateWeeklyPoint(user.getUserId(), category.getPoint());

        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();

        log.info("requestBody : {}", dto.toLog()); // log 출력 전 photo 삭제
        return SuccessReturn(certification);
    }

    /*
     * 인증 등록 [Past]
     * Request Data : PastCertificationDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PostMapping("/past")
    public ResponseEntity registerPast(@Validated @RequestBody PastCertDTO dto) {
        boolean isMungple = (dto.getMungpleId() != 0);
        Certification certification = certService.register((isMungple) ? dto.toEntity(mungpleService.getMungpleById(dto.getMungpleId())) : dto.toEntity());

        // 인증시 획득 가능한 업적 있는지 확인 ( 멍플, 일반 구분 )
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(), isMungple);
        if (!earnAchievements.isEmpty()) {
            earnAchievements.forEach(achievement -> {
                Archive archive = Archive.builder()
                        .achievementsId(achievement.getAchievementsId())
                        .userId(dto.getUserId())
                        .isMain(0)
                        .build();
                archiveService.register(archive);
            });

            // 해당 인증이 업적에 영향을 주었는지 체크
            certification.setIsAchievements(true);
            certification.setAchievements(earnAchievements);
            certification = certService.register(certification);
        }

        return SuccessReturn(certification);
    }

    /*
     * 인증 수정
     * Request Data : CertificationModifyDTO
     * Response Data : 등록한 인증 데이터 반환
     */
    @PutMapping
    public ResponseEntity modify(@Validated @RequestBody ModifyCertDTO dto) {
        return SuccessReturn(certService.modify(dto));
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
        if (categoryCode.isBlank()) return ErrorReturn(ApiCode.PARAM_ERROR);
        return SuccessReturn(certService.getCertByCategory(userId, categoryCode, currentPage, pageSize, isDesc));
    }

    /*
     * 카테고리 별 인증 개수 반환 ( 유저 )
     * Request Data : userId
     * Response Data : 카테고리별 인증 개수 반환
     */
    @GetMapping(value = {"/category/count/{userId}","/category/count/"})
    public ResponseEntity getCategoryCount(@PathVariable Integer userId) {
        return SuccessReturn(certService.getCountByCategory(userId));
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
        Certification certification = certService.getCert(certificationId);

        // 사용자가 해당 Certification 좋아요 눌렀는지 체크.
        if (likeListService.hasLiked(userId, certificationId)) { // 좋아요 존재
            likeListService.delete(userId, certificationId);
            if (certification.getLikeCount() > 0) // 혹시 like_count가 0이거나 혹은 음수이면 miuns 시키지 않는다.
                certService.minusLikeCount(certificationId);
        } else {
            likeListService.register(userId, certificationId);
            certService.plusLikeCount(certificationId);
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
        return SuccessReturn(certService.getRecentCertificationList(userId));
    }

    /*
     * 모든 인증 리스트 페이징으로 반환 [ Main ]
     * Request Data : currentPage ( 현재 페이지 번호 ), pageSize ( 페이지 크기 )
     * Response Data : 인증 모두 조회 ( 페이징 처리 되어 있음 )
     */
    @GetMapping("/all")
    public ResponseEntity getPagingData(@RequestParam Integer userId, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        return SuccessReturn(certService.getCertificationAll(userId, currentPage, pageSize, true));
    }

    /*
     * 인증 삭제
     * Request Data : userId ( 삭제 요청 userId ), certificationId ( 삭제할 인증 )
     * 요청 userId랑 등록 userId랑 비교 해야 함.
     * Response Data : null
     */
    @DeleteMapping(value={"/{userId}/{certificationId}"})
    public ResponseEntity delete(@PathVariable Integer userId, @PathVariable Integer certificationId) {
        Certification certification = certService.getCert(certificationId);

        if(userId != certification.getUserId())
            return ErrorReturn(ApiCode.INVALID_USER_ERROR);

        certService.delete(certification);
        return SuccessReturn();
    }
}