package com.delgo.reward;


import com.delgo.reward.service.crawling.GetNaverMungplePhotoCrawlingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlingTest {

    @Autowired
    private GetNaverMungplePhotoCrawlingService naverCrawlingService;

    @Autowired

    @Test
    public void getNaverMungpleDetailPhotoCrawlingTest() throws Exception {
        String url = "https://map.naver.com/v5/search/%EB%B9%84%EC%8A%A4%ED%8A%B8%EB%A1%9C%EB%A7%88%EC%9D%B8/place/1047172387?c=15,0,0,0,dh&isCorrectAnswer=true";

        naverCrawlingService.crawlingProcess(url);
    }
}
