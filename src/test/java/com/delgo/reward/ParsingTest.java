package com.delgo.reward;

import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoRepository.MongoMungpleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class ParsingTest {

    @Autowired
    private MongoMungpleRepository mongoMungpleRepository;


    @Test
    public void checkDuplicated() {
        List<MongoMungple> mungpleList = mongoMungpleRepository.findAll();
        for(MongoMungple mungple : mungpleList){
            System.out.println(mungple.getMungpleId() + " name = " + mungple.getPlaceName());
        }
    }

    @Test
    public void textParsingTEST() {
      String text =  "강동구_애견동반카페_담금_menu_3";
      
      String[] text_arr = text.split("_");
        System.out.println("text_arr = " + Arrays.toString(text_arr));

        String order = text_arr[text_arr.length -1];
        System.out.println("order = " + order);

        String type = text_arr[text_arr.length - 2];

        switch (type){
            case "board":
                System.out.println("board");
            case "menu":
                System.out.println("menu");
            case "price":
                System.out.println("price");
            case "dog":
                System.out.println("dog");
            default:
                System.out.println("photo");
        }
    } 
}