package com.delgo.reward;


import com.delgo.reward.mongoService.MongoMungpleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoMungpleTest {

    @Autowired
    private MongoMungpleService mongoMungpleService;

//    @Test
//    public void makeMungpletoMongoTEST() {
//        mongoMungpleService.makeMungpletoMongo();
//    }

    @Test
    public void findWithin3KmTEST() {
        String latitude = "37.4704204";
        String longitude = "127.1412807";

        mongoMungpleService.findWithin3Km(latitude,longitude);
    }
}
