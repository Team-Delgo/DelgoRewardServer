package com.delgo.reward.mongoService;

import com.delgo.reward.mongoDomain.NaverPlace;
import com.delgo.reward.mongoRepository.NaverPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverPlaceService {
    private final NaverPlaceRepository naverPlaceRepository;

    public boolean checkDuplicate(NaverPlace naverPlace) {
        List<NaverPlace> result = naverPlaceRepository.findByPlaceNameAndAddress(naverPlace.getPlaceName(), naverPlace.getAddress());
        if (result.size() > 0) log.info("해당 Place : {}는 이미 저장되었습니다.", naverPlace.getPlaceName());

        return (result.size() > 0);
    }

    public void register(NaverPlace naverPlace) {
        naverPlaceRepository.save(naverPlace);
    }

    public void deleteDuplicate() {
        List<NaverPlace> naverPlaces = naverPlaceRepository.findAll();
        for(NaverPlace naverPlace :naverPlaces){
            List<NaverPlace> result = naverPlaceRepository.findByPlaceNameAndAddress(naverPlace.getPlaceName(), naverPlace.getAddress());
            if(result.size() >= 2)
//                naverPlaceRepository.delete(naverPlace);
                log.info("두개 이상 존재하는 result : {}", naverPlace.getPlaceName());
        }
    }
}
