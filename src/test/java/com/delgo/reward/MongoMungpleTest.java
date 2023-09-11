package com.delgo.reward;


import com.delgo.reward.domain.mungple.Mungple;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoDomain.mungple.MungpleDetail;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.repository.MungpleRepository;
import com.delgo.reward.service.ExcelParserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoMungpleTest {

    @Autowired
    private MongoMungpleService mongoMungpleService;

    @Autowired
    private MongoMungpleRepository mongoMungpleRepository;
    @Autowired
    private MungpleRepository mungpleRepository;
    @Autowired
    private MungpleDetailRepository mungpleDetailRepository;

    @Autowired
    private ExcelParserService excelParserService;

//    @Test
//    public void makeMungpletoMongoTEST() {
//        mongoMungpleService.makeMungpletoMongo();
//    }

    @Test
    public void deleteAllMongoMunple(){
        mongoMungpleRepository.deleteAll();
    }
    @Test
    public void saveAllMungpleAndDetailToMongo(){
        List<Mungple> mungpleList = mungpleRepository.findAll();
        for (Mungple mungple : mungpleList){
            Optional<MungpleDetail> mungpleDetailOptional = mungpleDetailRepository.findByMungpleId(mungple.getMungpleId());
            if(mungpleDetailOptional.isEmpty())
                continue;
            MungpleDetail mungpleDetail = mungpleDetailOptional.get();

            MongoMungple mongoMungple = MongoMungple
                    .builder()
                    .categoryCode(mungple.getCategoryCode())
                    .phoneNo(mungple.getPhoneNo())
                    .placeName(mungple.getPlaceName())
                    .placeNameEn(mungple.getPlaceNameEn())
                    .roadAddress(mungple.getRoadAddress())
                    .jibunAddress(mungple.getJibunAddress())
                    .geoCode(mungple.getGeoCode())
                    .pGeoCode(mungple.getPGeoCode())
                    .latitude(mungple.getLatitude())
                    .longitude(mungple.getLongitude())
                    .detailUrl(mungple.getDetailUrl())
                    .isActive(mungple.isActive())
                    .createdAt(LocalDateTime.now())
                    .location(new GeoJsonPoint(Double.parseDouble(mungple.getLongitude()), Double.parseDouble(mungple.getLatitude())))
                    .photoUrls(mungpleDetail.getPhotoUrls())
                    .enterDesc(mungpleDetail.getEnterDesc())
                    .acceptSize(mungpleDetail.getAcceptSize())
                    .businessHour(mungpleDetail.getBusinessHour())
                    .instaId(mungpleDetail.getInstaId())
                    .isParking(mungpleDetail.getIsParking())
                    .parkingInfo(mungpleDetail.getParkingInfo())
                    .residentDogName(mungpleDetail.getResidentDogName())
                    .residentDogPhoto(mungpleDetail.getResidentDogPhoto())
                    .representMenuTitle(mungpleDetail.getRepresentMenuTitle())
                    .representMenuPhotoUrls(mungpleDetail.getRepresentMenuPhotoUrls())
                    .isPriceTag(mungpleDetail.getIsPriceTag())
                    .priceTagPhotoUrls(mungpleDetail.getPriceTagPhotoUrls())
                    .build();

            mongoMungpleRepository.save(mongoMungple);
        }
    }

    @Test
    public void modifyPhotoUrlsTest() {
        int mungpleId = 2;
        List<String> photos = new ArrayList<>();
        photos.add("https://test.com");


//        mongoMungpleService.modifyPhotoUrls(mungpleId, photos);
    }

    @Test
    public void modifyMenuPhotoUrlsTest() {
        int mungpleId = 2;
        String url = "https:asfsadf";


//        mongoMungpleService.modifyMenuPhotoUrl(mungpleId, url);
    }

    @Test
    public void findWithin3KmTEST() {
        String latitude = "37.4704204";
        String longitude = "127.1412807";

        mongoMungpleService.findWithin3Km(latitude,longitude);
    }
}
