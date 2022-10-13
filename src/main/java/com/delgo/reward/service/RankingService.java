package com.delgo.reward.service;

import com.delgo.reward.domain.ranking.RankingCategory;
import com.delgo.reward.domain.ranking.RankingPoint;
import com.delgo.reward.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RankingService {
    private final JDBCTemplateRankingRepository jdbcTemplateRankingRepository;
    private final CertificationRepository certificationRepository;
    private final RankingCategoryRepository rankingCategoryRepository;
    private final RankingPointRepository rankingPointRepository;
    private final UserRepository userRepository;

    public void initWeeklyPoint(){
        rankingCategoryRepository.deleteAll();
        rankingPointRepository.deleteAll();
        userRepository.initAllWeeklyPoint();
    }

    public RankingPoint getByPointRanking(int userId){
        return rankingPointRepository.findByUserId(userId)
                .orElseThrow(() -> new NullPointerException("NOT FOUND ranking"));
    }

    public int getByCategoryRanking(int userId, String categoryCode){
        int userRanking = rankingCategoryRepository.findByUserIdAndCategoryCode(userId, categoryCode);
        return userRanking;
    }

    public List<RankingPoint> getTopPointRankingByGeoCode(String geoCode){
        List<RankingPoint> topPointRankingList =jdbcTemplateRankingRepository.findTopPointRankingByGeoCode(geoCode);
        return topPointRankingList;
    }

    public List<RankingPoint> getPointRankingByGeocode(String geoCode){
        List<RankingPoint> pointRankingList = jdbcTemplateRankingRepository.findPointRankingByGeoCode(geoCode);
        return pointRankingList;
    }

    public void rankingByPoint(){
        List<RankingPoint> rankingPointList = jdbcTemplateRankingRepository.findRankingByPoint();
        for(RankingPoint rankingPoint : rankingPointList){
            RankingPoint newRankingPoint = RankingPoint.builder().userId(rankingPoint.getUserId()).ranking(rankingPoint.getRanking()).weeklyPoint(rankingPoint.getWeeklyPoint()).geoCode(rankingPoint.getGeoCode()).build();
            rankingPointRepository.save(newRankingPoint);
        }
    }

    public void rankingByCategoryCode(String categoryCode){
        List<RankingCategory> rankingCategoryList = jdbcTemplateRankingRepository.findRankingByCategory(categoryCode);
        for(RankingCategory rankingCategory : rankingCategoryList){
            RankingCategory newRankingCategory = RankingCategory.builder().userId(rankingCategory.getUserId()).ranking(rankingCategory.getRanking()).geoCode(rankingCategory.getGeoCode()).categoryCode(rankingCategory.getCategoryCode()).build();
            rankingCategoryRepository.save(newRankingCategory);
        }
    }

}
