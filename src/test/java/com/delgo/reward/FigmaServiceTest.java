package com.delgo.reward;

import com.delgo.reward.service.FigmaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

    @Test
    public void figmaPhotoUploadTest() throws UnsupportedEncodingException {
        String nodeId = "213:55";

        Map<String, String> ids_map = figmaService.getImageIdFromFigma(nodeId);
        Map<String, String> imageUrlMap = figmaService.getImageUrlFromFigma(ids_map);

        // typeListMap 초기화
        Map<String, ArrayList<String>> typeListMap = Map.of(
                "thumbnail", new ArrayList<>(),
                "menu", new ArrayList<>(),
                "menu_board", new ArrayList<>(),
                "price_tag", new ArrayList<>(),
                "dog", new ArrayList<>()
        );

        // imageUrlMap을 Type에 맞게 각 List에 저장
        figmaService.processImages(imageUrlMap, typeListMap);
    }
}
