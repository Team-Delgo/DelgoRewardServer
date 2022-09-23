package com.delgo.reward.service;

import com.delgo.reward.domain.ranking.RankingCategory;
import com.delgo.reward.domain.ranking.RankingPoint;
import com.delgo.reward.repository.CertificationRepository;
import com.delgo.reward.repository.JDBCTemplateRankingRepository;
import com.delgo.reward.repository.RankingCategoryRepository;
import com.delgo.reward.repository.RankingPointRepository;
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

    public int getByPointRanking(int userId){
        int userRanking = rankingPointRepository.findByUserId(userId);

        return userRanking;
    }

    public int getByCategoryRanking(int userId, String categoryCode){
        int userRanking = rankingCategoryRepository.findByUserIdAndCategoryCode(userId, categoryCode);
        return userRanking;
    }

    public void rankingByPoint(){
        List<RankingPoint> rankingPointList = jdbcTemplateRankingRepository.findRankingByPoint();
        for(RankingPoint rankingPoint : rankingPointList){
            RankingPoint newRankingPoint = RankingPoint.builder().userId(rankingPoint.getUserId()).ranking(rankingPoint.getRanking()).geoCode(rankingPoint.getGeoCode()).build();
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
