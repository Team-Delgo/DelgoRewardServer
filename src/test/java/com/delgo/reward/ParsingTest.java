package com.delgo.reward;

import com.delgo.reward.token.service.FcmService;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.mungple.repository.MungpleRepository;
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
    private FcmService fcmService;
    @Autowired
    private MungpleRepository mungpleRepository;


    @Test
    public void checkDuplicated() {
        List<Mungple> mungpleList = mungpleRepository.findAll();
        for(Mungple mungple : mungpleList){
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
    @Test
    public void test(){
        int userId = 364;
        fcmService.push(userId, "test");
    }
}