package com.delgo.reward.comm.googlesheet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class FigmaServiceTest {
    @Autowired
    FigmaService figmaService;

    @Test
    void getImageIdFromFigma() {
        // given
        String nodeId = "398:1388";
        String baseName = "baseName";

        // when
        Map<String, String> imageIdMap = figmaService.getImageIdFromFigma(nodeId, baseName);
        for (Map.Entry<String, String> entry : imageIdMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
        // then
    }

    @Test
    void getImageUrlFromFigma() {
        // given
        String nodeId = "398:1388";
        String baseName = "baseName";

        // when
        Map<String, String> imageIdMap = figmaService.getImageIdFromFigma(nodeId, baseName);
        Map<String, String> imageUrlMap = figmaService.getImageUrlFromFigma(imageIdMap);
        for (Map.Entry<String, String> entry : imageUrlMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // then
    }
}