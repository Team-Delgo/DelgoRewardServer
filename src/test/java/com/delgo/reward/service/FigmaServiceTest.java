package com.delgo.reward.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FigmaServiceTest {
    @Autowired
    FigmaService figmaService;

    @Test
    public void getComponentTest(){
        String nodeId = "5633:29979";

        figmaService.getImageIdFromFigma(nodeId);
    }

    @Test
    public void getImageTest(){
        String nodeId = "5633:29979";

        Map<String, String> ids_map = figmaService.getImageIdFromFigma(nodeId);
        figmaService.getImageUrlFromFigma(ids_map);
    }
}
