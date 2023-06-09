package com.delgo.reward;


import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.service.ExcelParserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoMungpleTest {

    @Autowired
    private MongoMungpleService mongoMungpleService;

    @Autowired
    private ExcelParserService excelParserService;

//    @Test
//    public void makeMungpletoMongoTEST() {
//        mongoMungpleService.makeMungpletoMongo();
//    }

    @Test
    public void modifyEnterDescTest() {
        int mungpleId = 5;
        String desc = "상주견 모르네가 대형견을 무서워해서 대형견 친구들은 아쉽게도 함께 하지 못해요.\n" +
                "카페앞 1대 주차 할 수 있어요. 미리 전화를 하고 오시면 주차 여부를 안내해 드릴 수 있어요.\n" +
                "펫티캣을 지켜서 반려인 비반려인 모두 행복 할 수 있는 공간을 만들어주세요.";


        mongoMungpleService.modifyEnterDesc(mungpleId, desc);
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

    @Test
    public void excelParseTest() throws IOException {
        String filePath = "C:\\testPhoto\\delgo.xlsx";
        excelParserService.parseExcelFile(filePath);
    }
}
