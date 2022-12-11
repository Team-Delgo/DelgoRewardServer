package com.delgo.reward;


import com.delgo.reward.service.crawling.GetDogCodeCrawlingService;
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

    @Test
    public void getDogTest() throws Exception {
        String url = "https://ddnews.co.kr/%EA%B0%95%EC%95%84%EC%A7%80%EC%A2%85%EB%A5%98-%EA%B0%9C%EC%A2%85%EB%A5%98/";

//        String test = "DT00" + 15;
//        System.out.println(test);
        crawlingService.crawlingProcess(url);
    }
}
