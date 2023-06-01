package com.delgo.reward;


import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.service.ExcelParserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

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
    public void modifyMungpleDetailTest() {
        mongoMungpleService.modifyMungpleDetail(2,"똑똑이2");
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
