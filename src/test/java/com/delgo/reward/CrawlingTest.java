package com.delgo.reward;


import com.delgo.reward.service.crawling.GetDogCodeCrawlingService;
import com.delgo.reward.service.crawling.GetNaverMungpleCrawlingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlingTest {

    @Autowired
    private GetDogCodeCrawlingService crawlingService;

    @Autowired
    private GetNaverMungpleCrawlingService naverCrawlingService;

    @Test
    public void getCrawlingTest() throws Exception {
        String url = "https://map.naver.com/v5/search/%EC%8B%A0%EC%82%AC%EC%97%AD?c=15,0,0,0,dh";

        naverCrawlingService.crawlingProcess(url);
    }
}
