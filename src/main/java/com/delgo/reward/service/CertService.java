package com.delgo.reward.service;


import com.delgo.reward.domain.Certification;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CertService {

    private final UserService userService;
    private final LikeListService likeListService;

    private final CertRepository certRepository;
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;

    private final LocalDateTime start = LocalDate.now().atTime(0, 0, 0);
    private final LocalDateTime end = LocalDate.now().atTime(0, 0, 0).plusDays(1);

    // 전체 Certification 리스트 조회
    public Slice<Certification> getCertificationAll(int userId, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = (isDesc)
                ? PageRequest.of(currentPage, pageSize,  Sort.by("regist_dt").descending()) // 내림차순 정렬
                : PageRequest.of(currentPage, pageSize,  Sort.by("regist_dt")); // 오름차순 정렬

        Slice<Certification> pagingData = certRepository.findAllByPaging(userId, pageRequest);

        for(Certification certification : pagingData.getContent()) {
            certification.setUser(userService.getUserByUserId(certification.getUserId()));
            certification.setIsLike((likeListService.hasLiked(userId, certification.getCertificationId())));
        }

        return pagingData;
    }

    // categoryCode & userId로 Certification 리스트 조회
    public Slice<Certification> getCertificationByUserIdAndCategoryCode(int userId, String categoryCode, int currentPage, int pageSize, boolean isDesc ) {
        PageRequest pageRequest = (isDesc)
                ? PageRequest.of(currentPage, pageSize,  Sort.by("registDt").descending()) // 내림차순 정렬
                : PageRequest.of(currentPage, pageSize,  Sort.by("registDt")); // 오름차순 정렬

        Slice<Certification> pagingData = certRepository.findByUserIdAndCategoryCode(userId, categoryCode, pageRequest);

        for(Certification certification : pagingData.getContent()) {
            certification.setUser(userService.getUserByUserId(certification.getUserId()));
            certification.setIsLike((likeListService.hasLiked(userId, certification.getCertificationId())));
        }

        return pagingData;
    }

    // userId로 Certification 조회
    public List<Certification> getCertificationByUserId(int userId) {
        return certRepository.findByUserId(userId);
    }


    public Slice<Certification> getCertificationByUserIdPaging(int userId, int currentPage, int pageSize, boolean isDesc) {
        PageRequest pageRequest = (isDesc)
                ? PageRequest.of(currentPage, pageSize,  Sort.by("registDt").descending()) // 내림차순 정렬
                : PageRequest.of(currentPage, pageSize,  Sort.by("registDt")); // 오름차순 정렬

        Slice<Certification> pagingData = certRepository.findByUserId(userId, pageRequest);

        for(Certification certification : pagingData.getContent()) {
            certification.setUser(userService.getUserByUserId(certification.getUserId()));
            certification.setIsLike((likeListService.hasLiked(userId, certification.getCertificationId())));
        }

        return pagingData;
    }

    // CertificationId로 Certification 조회
    public Certification getCertificationByCertificationId(int certificationId) {
        return certRepository.findByCertificationId(certificationId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND Certification"));
    }

    // 최근 2개 조회
    public List<Certification> getRecentCertificationList(int userId) {
        List<Certification> list = certRepository.findTop2ByOrderByRegistDtDesc(userId);

        for(Certification certification : list) {
            certification.setUser(userService.getUserByUserId(certification.getUserId()));
            certification.setIsLike((likeListService.hasLiked(userId, certification.getCertificationId())));
        }

        return list;
    }

    // Certification 등록
    public Certification registerCertification(Certification certification) {
        return certRepository.save(certification);
    }

    // Certification 수정
    public Certification modifyCertification(Certification certification) {
        return certRepository.save(certification);
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

    // userId & categoryCode 만족하는 인증 개수
    public int countCertByUserIdAndCategoryCode(int userId, String categoryCode) {
        return certRepository.countByUserIdAndCategoryCode(userId, categoryCode);
    }

    // userId & categoryCode & mungple Id 만족하는 인증 개수
    public int countCertByUserIdAndCategoryCodeAndMungpleId(int userId, String categoryCode, int mungpleId) {
        return certRepository.countByUserIdAndCategoryCodeAndMungpleId(userId, categoryCode, mungpleId);
    }

    // 6시간 이내 같은 장소 인증 불가능 ( 멍플만 )
    public boolean checkMungpleCertRegister(int userId, int mungpleId, boolean isLive) {
        List<Certification> list = certRepository.findByUserIdAndMungpleIdAndIsLiveAndRegistDtBetween(userId, mungpleId, isLive, start, end);
        List<Certification> sortedList = list.stream().sorted(Comparator.comparing(Certification::getRegistDt).reversed()).collect(Collectors.toList());

        if (list.size() == 0)
            return true;

        // 최근 등록시간이랑 now 비교
        LocalDateTime recentRegistDt = sortedList.get(0).getRegistDt();
        long diff = ChronoUnit.SECONDS.between(recentRegistDt, LocalDateTime.now());

        // 최근 등록시간이랑 비교시간이 6시간(21600초)이내일 경우 오류 반환
        return diff > 21600;
    }

    // 하루에 같은 카테고리 5번 이상 인증 불가능
    public boolean checkCertRegister(int userId, String categoryCode, boolean isLive) {
        List<Certification> list = certRepository.findByUserIdAndCategoryCodeAndIsLiveAndRegistDtBetween(userId, categoryCode, isLive, start, end);
        return list.size() < 5;
    }

    public void delete(Certification certification){
        certRepository.delete(certification);
    }

    // Live Certification 조회
    public List<Certification> getLive(int userId) {
        return certRepository.findByUserIdAndIsLive(userId, true);
    }

    // Past Certification 조회
    public List<Certification> getPast(int userId) {
        return certRepository.findByUserIdAndIsLive(userId, false);
    }

}
