package com.delgo.reward.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FigmaServiceTest {
    @Autowired
    FigmaService figmaService;

    @Test
    public void getComponentTest(){
        String nodeId = "5633:29979";

        figmaService.getComponent(nodeId);
    }

    @Test
    public void getImageTest(){
        String nodeId = "5633:29979";

        Map<String, ArrayList<String>> ids_map = figmaService.getComponent(nodeId);

        // 모든 키에 대해 반복합니다.
        for (String key : ids_map.keySet()) {
            ArrayList<String> ids = ids_map.get(key);

            // 해당 키에 대한 ID 리스트를 figmaService.getImage()에 전달합니다.
            if (!ids.isEmpty()) {
                System.out.println("key: " + key);
                figmaService.getImage(ids);
            }
        }
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
