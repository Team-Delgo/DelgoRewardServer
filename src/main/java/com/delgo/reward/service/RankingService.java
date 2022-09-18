package com.delgo.reward.service;

import com.delgo.reward.domain.Ranking;
import com.delgo.reward.repository.CertificationRepository;
import com.delgo.reward.repository.JDBCTemplateRankingRepository;
import com.delgo.reward.repository.RankingRepository;
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
    private final CertificationRepository certificationRepository;
    private final RankingRepository rankingRepository;

    public void getByPoint(){
        List<Ranking> rankingList = jdbcTemplateRankingRepository.findRankingByPoint();
        for(Ranking ranking: rankingList){
            Ranking newRanking = Ranking.builder().userId(ranking.getUserId()).ranking(ranking.getRanking()).geoCode(ranking.getGeoCode()).categoryCode(ranking.getCategoryCode()).build();
            rankingRepository.save(newRanking);
        }
    }

    public void getByCategoryCode(String categoryCode){

    }

}
