package com.delgo.reward.service;

import com.delgo.reward.domain.ranking.RankingCategory;
import com.delgo.reward.domain.ranking.RankingPoint;
import com.delgo.reward.dto.RankingByPointDTO;
import com.delgo.reward.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RankingService {
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;
    private final RankingCategoryRepository rankingCategoryRepository;
    private final RankingPointRepository rankingPointRepository;
    private final UserRepository userRepository;

    // 주 1회 이전 주간 포인트 초기화
    public void initLastWeeklyPoint(){
        rankingCategoryRepository.deleteAll();
        rankingPointRepository.deleteAll();
        userRepository.initLastWeeklyPoint();
    }

    // 이전 주간 포인트 0으로 세팅
    public void setLastWeeklyPoint(){
        userRepository.setLastWeeklyPoint();
    }

    // 주간 포인트 0으로 초기화
    public void initWeeklyPoint(){
        userRepository.initWeeklyPoint();
    }

    // 이전 주간 랭킹 세팅
    public void setLastRanking(){
        jdbcTemplateRankingRepository.setLastRanking();
    }

    public RankingPoint getByPointRanking(int userId){
        return rankingPointRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND ranking"));
    }

    public int getByCategoryRanking(int userId, String categoryCode){
        int userRanking = rankingCategoryRepository.findByUserIdAndCategoryCode(userId, categoryCode);
        return userRanking;
    }

    public List<RankingByPointDTO> getTopPointRankingByGeoCode(String geoCode){
        List<RankingByPointDTO> topPointRankingList =jdbcTemplateRankingRepository.findTopPointRankingByGeoCode(geoCode);
        return topPointRankingList;
    }

    public List<RankingByPointDTO> getPointRankingByGeocode(String geoCode){
        List<RankingByPointDTO> pointRankingList = jdbcTemplateRankingRepository.findPointRankingByGeoCode(geoCode);
        return pointRankingList;
    }

    // 포인트 별 랭킹 매기기
    public void rankingByPoint(){
        List<RankingPoint> rankingPointList = jdbcTemplateRankingRepository.findRankingByPoint();
        for(RankingPoint rankingPoint : rankingPointList){
            RankingPoint newRankingPoint = RankingPoint.builder().userId(rankingPoint.getUserId()).ranking(rankingPoint.getRanking()).weeklyPoint(rankingPoint.getWeeklyPoint()).geoCode(rankingPoint.getGeoCode()).build();
            rankingPointRepository.save(newRankingPoint);
        }
    }

    // 카테고리 별 랭킹 매기기
    public void rankingByCategoryCode(String categoryCode){
        List<RankingCategory> rankingCategoryList = jdbcTemplateRankingRepository.findRankingByCategory(categoryCode);
        for(RankingCategory rankingCategory : rankingCategoryList){
            RankingCategory newRankingCategory = RankingCategory.builder().userId(rankingCategory.getUserId()).ranking(rankingCategory.getRanking()).geoCode(rankingCategory.getGeoCode()).categoryCode(rankingCategory.getCategoryCode()).build();
            rankingCategoryRepository.save(newRankingCategory);
        }
    }

}
