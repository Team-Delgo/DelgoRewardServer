package com.delgo.reward.service;

import com.delgo.reward.domain.Ranking;
import com.delgo.reward.dto.RankingByPointDTO;
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
        List<RankingByPointDTO> rankingByPointDTOS = jdbcTemplateRankingRepository.findRankingByPoint();
        for(RankingByPointDTO rankingByPointDTO: rankingByPointDTOS){
            Ranking ranking = Ranking.builder().userId(rankingByPointDTO.getUserId()).ranking(rankingByPointDTO.getRanking()).geoCode("101000").categoryCode("CA000").build();
            rankingRepository.save(ranking);
        }
    }

    public void getByCategoryCode(String categoryCode){

    }

}
