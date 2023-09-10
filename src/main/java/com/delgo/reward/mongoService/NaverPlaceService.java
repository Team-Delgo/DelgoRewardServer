package com.delgo.reward.mongoService;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.comm.ncp.GeoService;
import com.delgo.reward.domain.common.Location;
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
    private final GeoService geoService;
//    private final MungpleService mungpleService;
    private final MongoMungpleService mongoMungpleService;

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
        log.info("count :{} ", naverPlaces.size());
        int i = 1;
        for(NaverPlace naverPlace :naverPlaces){
            List<NaverPlace> result = naverPlaceRepository.findByPlaceNameAndAddress(naverPlace.getPlaceName(), naverPlace.getAddress());
            if(result.size() >= 2)
//                naverPlaceRepository.delete(naverPlace);
                log.info("{} 두개 이상 존재하는 result : {}",i++, naverPlace.getPlaceName());

            log.info("{} 정상입니다. : {}",i++, naverPlace.getPlaceName());
        }
    }

    public void getNaverPlaces() {
        List<NaverPlace> naverPlaces = naverPlaceRepository.findAll();
        for(int i = 0; i< naverPlaces.size(); i++){
            NaverPlace np = naverPlaces.get(i);
            Location location = geoService.getGeoData(np.getAddress());
            if (mongoMungpleService.isMungpleExisting(np.getAddress())) {
                log.info(" {} 이미 저장한 멍플입니다. : {}", i, np.getPlaceName());
                continue;
            }
            CategoryCode categoryCode = determineCategoryCode(np.getCategory());
//            Mungple mungple = mungpleService.register(np.toMungple(location, categoryCode.getCode()));
//            log.info("{} mungple : {}", i, mungple);
        }
    }

    public CategoryCode determineCategoryCode(String category) {
        String[][] words = {
                {
                        "카페", "케이크", "커피", "샌드위치", "아이스크림", "와플", "빙수"
                },
                {
                        "요리", "음식", "양식", "구이", "한식", "회", "브런치", "피자", "주점", "술집", "와인", "식당",
                        "중식당", "베이커리", "닭갈비", "백숙", "일식당", "파스타전문", "맥주", "치킨", "포장마차", "쌈밥",
                        "한정식", "곱창", "바", "백반", "양갈비", "족발", "햄버거", "샤브샤브", "꼬치", "카레", "샐러드",
                        "찌개", "찜닭", "닭볶음탕", "국수", "이자카야", "칼국수", "돈가스", "막국수", "스테이크", "해물찜"
                }
        };

        CategoryCode[] categoryCodes = {CategoryCode.CA0002, CategoryCode.CA0003};

        for (int i = 0; i < words.length; i++) {
            for (String word : words[i]) {
                if (category.contains(word)) {
                    return categoryCodes[i];
                }
            }
        }

        return CategoryCode.CA9999;
    }
}
