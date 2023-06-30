package com.delgo.reward;


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
public class ExcelParserTest {

    @Autowired
    private ExcelParserService excelParserService;

    @Test
    public void excelParseOfCafeTest() throws IOException {
        String filePath = "C:\\testPhoto\\delgo.xlsx";

        excelParserService.parseExcelFileOfCafe(filePath);
    }

    @Test
    public void thumbnailImgConvertAndUploadTest() throws IOException {
        // given
        String menuUrl = "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/notwebp/올티드 커피_1.png";
        List<String> urls = new ArrayList<>();
        urls.add(menuUrl);

        excelParserService.thumbnailNCPUpload("test", urls);
    }

    @Test
    public void menuImgConvertAndUploadTest() throws IOException {
        // given
        String menuUrl = "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/notwebp/올티드 커피_1.png";
        List<String> urls = new ArrayList<>();
        urls.add(menuUrl);

        excelParserService.menuNCPUpload("test", urls);
    }

    @Test
    public void menuBoardImgConvertAndUploadTest() throws IOException {
        // given
        String menuUrl = "https://kr.object.ncloudstorage.com/reward-detail-thumbnail/notwebp/올티드 커피_1.png";
        List<String> urls = new ArrayList<>();
        urls.add(menuUrl);

        excelParserService.menuBoardNCPUpload("test", urls);
    }
}
