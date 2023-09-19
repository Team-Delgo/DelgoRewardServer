package com.delgo.reward;


import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoMungpleTest {

    @Autowired
    private MongoMungpleService mongoMungpleService;

    @Autowired
    private MongoMungpleRepository mongoMungpleRepository;

    @Autowired
    private MungpleDetailRepository mungpleDetailRepository;

    @Test
    public void deleteAllMongoMunple(){
        mongoMungpleRepository.deleteAll();
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
