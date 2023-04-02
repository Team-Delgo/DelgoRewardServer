package com.delgo.reward;


import com.delgo.reward.mongoService.NaverPlaceService;
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

    @Autowired
    private NaverPlaceService naverService;

    @Test
    public void getCrawlingTest() throws Exception {
        String url = "https://map.naver.com/v5/search/경주터미널?c=15,0,0,0,dh&isCorrectAnswer=true";

        naverCrawlingService.crawlingProcess(url);
    }

    @Test
    public void getCrawlingTest2() throws Exception {
        String url = "https://map.naver.com/v5/search/가평역?c=15,0,0,0,dh&isCorrectAnswer=true";

        naverCrawlingService.crawlingProcess(url);
    }

    @Test
    public void getCrawlingTest3() throws Exception {
        String url = "https://map.naver.com/v5/search/인천역?c=15,0,0,0,dh&isCorrectAnswer=true";

        naverCrawlingService.crawlingProcess(url);
    }

    @Test
    public void getCrawlingTest4() throws Exception {
        String url = "https://map.naver.com/v5/search/동탄역?c=15,0,0,0,dh&isCorrectAnswer=true";

        naverCrawlingService.crawlingProcess(url);
    }

    @Test
    public void getCrawlingTest5() throws Exception {
        String url = "https://map.naver.com/v5/search/대전역?c=15,0,0,0,dh&isCorrectAnswer=true";

        naverCrawlingService.crawlingProcess(url);
    }

    @Test
    public void getCrawlingTest6() throws Exception {
        String url = "https://map.naver.com/v5/search/광주광역시청?c=15,0,0,0,dh&isCorrectAnswer=true";

        naverCrawlingService.crawlingProcess(url);
    }

    @Test
    public void getCrawlingTest7() throws Exception {
        naverService.deleteDuplicate();
    }
}
