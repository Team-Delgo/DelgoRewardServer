package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.Certification;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.achievements.Archive;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.dto.certification.LiveCertDTO;
import com.delgo.reward.dto.certification.ModifyCertDTO;
import com.delgo.reward.dto.certification.PastCertDTO;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.repository.JDBCTemplateRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertService {

    // Service
    private final UserService userService;
    private final PointService pointService;
    private final PhotoService photoService;
    private final ArchiveService archiveService;
    private final MungpleService mungpleService;
    private final RankingService rankingService;
    private final LikeListService likeListService;
    private final ReverseGeoService reverseGeoService;
    private final AchievementsService achievementsService;

    // Repository
    private final CertRepository certRepository;
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;

    private final LocalDateTime start = LocalDate.now().atTime(0, 0, 0);
    private final LocalDateTime end = LocalDate.now().atTime(0, 0, 0).plusDays(1);

    // Certification 등록
    public Certification register(Certification certification) {
        return certRepository.save(certification);
    }

    // Past 등록
    public Certification registerLive(LiveCertDTO dto) {
        Location location = reverseGeoService.getReverseGeoData(new Location(dto.getLatitude(), dto.getLongitude()));
        Certification certification = register(dto.toEntity(location));

        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(),  dto.getMungpleId() != 0);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream().map(achievement ->
                            Archive.builder()
                                    .achievementsId(achievement.getAchievementsId())
                                    .userId(dto.getUserId())
                                    .isMain(0)
                                    .build())
                    .collect(Collectors.toList())
            );

            certification.setIsAchievements(true);
            certification.setAchievements(earnAchievements);
        }

        // 사진 파일 저장 추가
        String photoUrl = photoService.uploadCertEncodingFile(certification.getCertificationId(), dto.getPhoto());
        register(certification.setPhotoUrl(photoUrl));

        // Point 부여
        User user = userService.getUserById(dto.getUserId());
        CategoryCode category = CategoryCode.valueOf(dto.getCategoryCode());
        pointService.updateAccumulatedPoint(user.getUserId(), category.getPoint());
        pointService.updateWeeklyPoint(user.getUserId(), category.getPoint());

        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();

        log.info("requestBody : {}", dto.toLog()); // log 출력 전 photo 삭제
        return certification;
    }

    // Past 등록
    public Certification registerPast(PastCertDTO dto) {
        boolean isMungple = (dto.getMungpleId() != 0);
        Certification certification = register((isMungple) ? dto.toEntity(mungpleService.getMungpleById(dto.getMungpleId())) : dto.toEntity());

        // 인증시 획득 가능한 업적 있는지 확인 ( 멍플, 일반 구분 )
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(), isMungple);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream().map(achievement ->
                            Archive.builder()
                                    .achievementsId(achievement.getAchievementsId())
                                    .userId(dto.getUserId())
                                    .isMain(0)
                                    .build())
                    .collect(Collectors.toList())
            );

            // 해당 인증이 업적에 영향을 주었는지 체크
            certification.setIsAchievements(true);
            certification.setAchievements(earnAchievements);
            certification = register(certification);
        }

        return certification;
    }

    // Certification 수정
    public Certification modify(ModifyCertDTO dto) {
        return certRepository.save(getCert(dto.getCertificationId()).modify(dto.getDescription()));
    }

    // Certification 삭제
    public void delete(Certification certification) {
        certRepository.delete(certification);
    }

    // 전체 Certification 리스트 조회
    public Slice<Certification> getCertAll(int userId, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = (isDesc)
                ? PageRequest.of(currentPage, pageSize, Sort.by("regist_dt").descending()) // 내림차순 정렬
                : PageRequest.of(currentPage, pageSize, Sort.by("regist_dt")); // 오름차순 정렬

        Slice<Certification> pagingData = certRepository.findAllByPaging(userId, pageRequest);

        for (Certification certification : pagingData.getContent()) {
            certification.setUser(userService.getUserById(certification.getUserId()));
            certification.setIsLike((likeListService.hasLiked(userId, certification.getCertificationId())));
        }

        return pagingData;
    }

    // 카테고리 별 조회
    public Slice<Certification> getCertByCategory(int userId, String categoryCode, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, (isDesc) ? Sort.by("registDt").descending() : Sort.by("registDt"));

        Slice<Certification> certifications = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certRepository.findByUserIdAndCategoryCode(userId, categoryCode, pageRequest)
                : certRepository.findByUserId(userId, pageRequest);

        certifications.getContent().forEach(cert -> {
            cert.setUser(userService.getUserById(cert.getUserId()));
            cert.setIsLike((likeListService.hasLiked(userId, cert.getCertificationId())));
        });

        return certifications;
    }

    // 카테고리 별 개수 조회
    public Map<String, Long> getCountByCategory(int userId) {
        Map<String, Long> returnMap = new HashMap<>();
        for (CategoryCode categoryCode : CategoryCode.values())
            returnMap.put(categoryCode.getValue(), getCertByUserId(userId).stream().filter(c -> c.getCategoryCode().equals(categoryCode.getCode())).count());

        return returnMap;
    }

    // 최근 2개 인증 조회
    public List<Certification> getTwoRecentCert(int userId) {
        return certRepository.findTwoRecentCert(userId).stream().peek(cert -> {
            cert.setUser(userService.getUserById(cert.getUserId()));
            cert.setIsLike((likeListService.hasLiked(userId, cert.getCertificationId())));
        }).collect(Collectors.toList());
    }

    // 하루에 같은 카테고리 5번 이상 인증 불가능 체크
    public boolean checkCategoryCountIsFive(int userId, String categoryCode, boolean isLive) {
        List<Certification> list = certRepository.findByUserIdAndCategoryCodeAndIsLiveAndRegistDtBetween(userId, categoryCode, isLive, start, end);
        return list.size() < 5;
    }

    // Live Certification 조회
    public List<Certification> getLive(int userId) {
        return certRepository.findByUserIdAndIsLive(userId, true);
    }

    // Past Certification 조회
    public List<Certification> getPast(int userId) {
        return certRepository.findByUserIdAndIsLive(userId, false);
    }

    // Id로 Certification 조회
    public Certification getCert(int certificationId) {
        return certRepository.findById(certificationId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Certification id : " + certificationId));
    }

    // userId로 Certification 조회
    public List<Certification> getCertByUserId(int userId) {
        return certRepository.findByUserId(userId);
    }

    public void like(int userId, int certificationId) {
        // USER , Certification 존재 여부 체크
        userService.getUserById(userId);
        getCert(certificationId);

        // 사용자가 해당 Certification 좋아요 눌렀는지 체크.
        if (likeListService.hasLiked(userId, certificationId)) { // 좋아요 존재
            likeListService.delete(userId, certificationId);
            minusLikeCount(certificationId);
        } else {
            likeListService.register(userId, certificationId);
            plusLikeCount(certificationId);
        }
    }

    // Like Plus Count + 1
    public void plusLikeCount(int certificationId) {
        jdbcTemplateRankingRepository.plusLikeCount(certificationId);
    }

    // Like Minus Count + 1
    public void minusLikeCount(int certificationId) {
        jdbcTemplateRankingRepository.minusLikeCount(certificationId);
    }

    // Comment Count + 1
    public void plusCommentCount(int certificationId) {
        jdbcTemplateRankingRepository.plusCommentCount(certificationId);
    }

    // Comment Count - 1
    public void minusCommentCount(int certificationId) {
        jdbcTemplateRankingRepository.minusCommentCount(certificationId);
    }

    // 6시간 이내 같은 장소 인증 불가능 ( 멍플만 )
    public boolean checkContinueRegist(int userId, int mungpleId, boolean isLive) {
        List<Certification> certifications = certRepository.findByUserIdAndMungpleIdAndIsLiveAndRegistDtBetween(userId, mungpleId, isLive, start, end).stream().sorted(Comparator.comparing(Certification::getRegistDt).reversed()).collect(Collectors.toList());
        if (certifications.isEmpty())
            return true;

        // 최근 등록시간이랑 비교시간이 6시간(21600초)이내일 경우 오류 반환
        return ChronoUnit.SECONDS.between(certifications.get(0).getRegistDt(), LocalDateTime.now()) > 21600;
    }

}
