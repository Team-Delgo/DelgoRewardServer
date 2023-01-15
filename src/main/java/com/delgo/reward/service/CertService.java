package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.like.LikeList;
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

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

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
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(), dto.getMungpleId() != 0);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream()
                    .map(achievement -> achievement.toArchive(dto.getUserId())).collect(Collectors.toList()));
            certification.setAchievements(earnAchievements);
        }

        // 사진 파일 저장 추가
        String photoUrl = photoService.uploadCertEncodingFile(certification.getCertificationId(), dto.getPhoto());
        register(certification.setPhotoUrl(photoUrl));
        // Point 부여
        pointService.givePoint(userService.getUserById(dto.getUserId()).getUserId(), CategoryCode.valueOf(dto.getCategoryCode()).getPoint());
        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();

        log.info("requestBody : {}", dto.toLog()); // log 출력 전 photo 삭제
        return certification;
    }

    // Past 등록
    public Certification registerPast(PastCertDTO dto) {
        boolean isMungple = (dto.getMungpleId() != 0);
        Certification certification = register((isMungple) ? dto.toEntity(mungpleService.getMungpleById(dto.getMungpleId())) : dto.toEntity());

        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnAchievements(dto.getUserId(), dto.getMungpleId() != 0);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream()
                    .map(achievement -> achievement.toArchive(dto.getUserId())).collect(Collectors.toList()));
            certification.setAchievements(earnAchievements);
        }

        return register(certification);
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

        Slice<Certification> certifications = certRepository.findAllByPaging(userId, pageRequest);
        certifications.getContent().forEach(cert -> setUserAndLike(userId, cert));
        return certifications;
    }

    // 카테고리 별 조회
    public Slice<Certification> getCertByCategory(int userId, String categoryCode, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, (isDesc) ? Sort.by("registDt").descending() :
                Sort.by("registDt"));

        Slice<Certification> certifications = (!categoryCode.equals(CategoryCode.TOTAL.getCode()))
                ? certRepository.findByUserIdAndCategoryCode(userId, categoryCode, pageRequest)
                : certRepository.findByUserId(userId, pageRequest);

        certifications.getContent().forEach(cert -> setUserAndLike(userId, cert));
        return certifications;
    }

    // 카테고리 별 개수 조회
    public Map<String, Long> getCountByCategory(int userId) {
        Map<String, Long> map = getCertByUserId(userId).stream().collect(groupingBy(cert -> CategoryCode.valueOf(cert.getCategoryCode()).getValue(),counting()));
        //*** putIfAbsent
        //- Key 값이 존재하는 경우 Map의 Value의 값을 반환하고, Key값이 존재하지 않는 경우 Key와 Value를 Map에 저장하고 Null을 반환합니다.
         for (CategoryCode categoryCode : CategoryCode.values())
             map.putIfAbsent(categoryCode.getValue(), 0L);
        return map;
    }

    // 유저별 전체 개수 조회
    public int getTotalCountByUser(int userId) {
        return certRepository.countByUserId(userId);
    }


    public Certification setUserAndLike(int userId, Certification cert) {
        return cert.setUserAndLike(
                userService.getUserById(cert.getUserId()), // USER
                likeListService.hasLiked(userId, cert.getCertificationId()), // User is Liked?
                likeListService.getLikeCount(cert.getCertificationId()) // Like Count
        );
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

    // 최근 2개 인증 조회
    public List<Certification> getTheLastTwoCert(int userId) {
        return certRepository.findTwoRecentCert(userId).stream()
                .peek(cert -> setUserAndLike(userId, cert)).collect(Collectors.toList());
    }

    // 좋아요 Check
    public void like(int userId, int certificationId, int ownerId) throws IOException {
        // 사용자가 해당 Certification 좋아요 눌렀는지 체크.
        boolean isLike = LikeListService.likeHashMap.getOrDefault(new LikeList(userId, certificationId), false);
        if (isLike)  // 좋아요 존재
            likeListService.unlike(userId, certificationId);
        else
            likeListService.like(userId, certificationId, ownerId);
    }

    // Comment Count + 1
    public void plusCommentCount(int certificationId) {
        jdbcTemplateRankingRepository.plusCommentCount(certificationId);
    }

    // Comment Count - 1
    public void minusCommentCount(int certificationId) {
        jdbcTemplateRankingRepository.minusCommentCount(certificationId);
    }

    // 하루에 같은 카테고리 5번 이상 인증 불가능 체크
    public boolean checkCategoryCountIsFive(int userId, String categoryCode, boolean isLive) {
        List<Certification> list = certRepository.findByUserIdAndCategoryCodeAndIsLiveAndRegistDtBetween(userId, categoryCode, isLive, start, end);
        return list.size() < 5;
    }

    // 6시간 이내 같은 장소 인증 불가능 ( 멍플만 )
//    public boolean checkContinueRegist(int userId, int mungpleId, boolean isLive) {
//        List<Certification> certifications =
//                certRepository.findByUserIdAndMungpleIdAndIsLiveAndRegistDtBetween(userId, mungpleId, isLive, start,
//                        end).stream().sorted(Comparator.comparing(Certification::getRegistDt).reversed()).collect(Collectors.toList());
//        if (certifications.isEmpty())
//            return true;
//
//        // 최근 등록시간이랑 비교시간이 6시간(21600초)이내일 경우 오류 반환
//        return ChronoUnit.SECONDS.between(certifications.get(0).getRegistDt(), LocalDateTime.now()) > 21600;
//    }
}
