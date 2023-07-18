package com.delgo.reward.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FigmaServiceTest {
    @Autowired
    FigmaService figmaService;

    @Test
    public void getComponentTest(){
        String figmaFileId = "5:39";

        figmaService.getComponent(figmaFileId);
    }

    @Test
    public void getImageTest(){
        String figmaFileId = "5:39";

        Map<String, ArrayList<String>> ids_map = figmaService.getComponent(figmaFileId);
        figmaService.getImage(ids_map.get("photo"));
    }

    @Test
    public void checkPhotoTypeTest(){
//        String input = "송파구_식당_WAAAH_1";
//        String input = "송파구_식당_WAAAH_menu_1";
//        String input = "송파구_식당_WAAAH_menu_board_1";
//        String input = "송파구_식당_WAAAH_price_1";
        String input = "송파구_식당_WAAAH_dog_1";
        figmaService.checkPhotoType(input);
    }
}
