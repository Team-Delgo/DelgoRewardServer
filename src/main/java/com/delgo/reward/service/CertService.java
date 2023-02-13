package com.delgo.reward.service;


import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.code.GeoCode;
import com.delgo.reward.comm.code.PGeoCode;
import com.delgo.reward.comm.ncp.ReverseGeoService;
import com.delgo.reward.domain.achievements.Achievements;
import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.domain.like.LikeList;
import com.delgo.reward.dto.certification.CertDTO;
import com.delgo.reward.dto.certification.ModifyCertDTO;
import com.delgo.reward.repository.CertRepository;
import com.delgo.reward.repository.JDBCTemplateRankingRepository;
import com.google.api.client.util.ArrayMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final ArchiveService archiveService;
    private final MungpleService mungpleService;
    private final RankingService rankingService;
    private final LikeListService likeListService;
    private final ReverseGeoService reverseGeoService;
    private final AchievementsService achievementsService;

    // Repository
    private final CertRepository certRepository;
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;


    // Certification DB에 저장
    public Certification save(Certification certification) {
        return certRepository.save(certification);
    }

    // Certification 등록
    public Certification register(CertDTO dto) {
        Certification certification = save(
                (dto.getMungpleId() == 0) // 일반 인증의 경우 - (위도,경도)로 주소 가져와서 등록해야 함.
                        ? dto.toEntity(reverseGeoService.getReverseGeoData(new Location(dto.getLatitude(), dto.getLongitude())))
                        : dto.toEntity(mungpleService.getMungpleById(dto.getMungpleId())));

        // 획득 가능한 업적 Check
        List<Achievements> earnAchievements = achievementsService.checkEarnedAchievements(dto.getUserId(), dto.getMungpleId() != 0);
        if (!earnAchievements.isEmpty()) {
            archiveService.registerArchives(earnAchievements.stream()
                    .map(achievement -> achievement.toArchive(dto.getUserId())).collect(Collectors.toList()));
            certification.setAchievements(earnAchievements);
        }

        // Point 부여
        pointService.givePoint(userService.getUserById(dto.getUserId()).getUserId(), CategoryCode.valueOf(dto.getCategoryCode()).getPoint());
        // 랭킹 실시간으로 집계
        rankingService.rankingByPoint();

        return certification;
    }

    // Certification 수정
    public Certification modify(ModifyCertDTO dto) {
        return getCert(dto.getCertificationId()).modify(dto.getDescription());
    }

    // Certification 삭제
    public void delete(int certificationId) {
        certRepository.deleteById(certificationId);
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

    // 유저별 전체 멍플 개수 조회
    public int getTotalCountOfMungpleByUser(int userId) {
        return certRepository.countOfMungpleByUser(userId);
    }


    public Certification setUserAndLike(int userId, Certification cert) {
        return cert.setUserAndLike(
                userService.getUserById(cert.getUserId()), // USER
                likeListService.hasLiked(userId, cert.getCertificationId()), // User is Liked?
                likeListService.getLikeCount(cert.getCertificationId()) // Like Count
        );
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

    // 최근 인증 조회
    public List<Certification> getRecentCert(int userId, int count) {
        return certRepository.findRecentCert(userId, count).stream()
                .peek(cert -> setUserAndLike(userId, cert)).collect(Collectors.toList());
    }

    // 노출 가능한 인증 조회
    public List<Certification> getExposedCert(int count) {
        return certRepository.findByIsExpose(count);
    }

    public Map<String, List<Certification>> test(int count){
        Map<String, List<Certification>> certByPGeoCode = new ArrayMap<>();
        List<Certification> certificationList = new ArrayList<>();

        for(PGeoCode p: PGeoCode.values()){
            if(p.getPGeoCode().equals(PGeoCode.P101000.getPGeoCode())){
                List<Certification> certificationListOfSongPa = certRepository.findByGeoCode(GeoCode.C101180.getGeoCode(), 3);
                List<Certification> certificationListNotOfSongPa = certRepository.findByPGeoCodeExceptGeoCode(p.getPGeoCode(), GeoCode.C101180.getGeoCode(), 3);

                certificationList = Stream.concat(certificationListOfSongPa.stream(), certificationListNotOfSongPa.stream())
                        .collect(Collectors.toList());
            } else {
                certificationList = certRepository.findByPGeoCode(p.getPGeoCode(), count);
            }
            if(certificationList.size() > 0){
                certByPGeoCode.put(p.getPGeoCode(), certificationList);
            }
        }

        return certByPGeoCode;
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
}
