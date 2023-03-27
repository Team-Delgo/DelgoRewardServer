package com.delgo.reward.mongoService;

import com.delgo.reward.mongoDomain.NaverPlace;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.mongoRepository.NaverPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverPlaceService {
    private final NaverPlaceRepository naverPlaceRepository;

    public void register(NaverPlace naverPlace){
        if (naverPlaceRepository.findByPlaceNameAndAddress(naverPlace.getPlaceName(), naverPlace.getAddress()).isPresent())
            log.info("해당 Place : {}는 이미 저장되었습니다.", naverPlace.getPlaceName());
        else
            naverPlaceRepository.save(naverPlace);
    }
}
