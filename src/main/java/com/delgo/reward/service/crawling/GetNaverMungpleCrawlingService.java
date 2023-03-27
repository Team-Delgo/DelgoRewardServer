package com.delgo.reward.service.crawling;

import com.delgo.reward.dto.MungpleDTO;
import com.delgo.reward.mongoDomain.NaverPlace;
import com.delgo.reward.mongoService.NaverPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GetNaverMungpleCrawlingService {

    @Value("${config.driverLocation}")
    String driverLocation;
    JavascriptExecutor js;
    private WebDriver driver;

    private final NaverPlaceService naverPlaceService;

    public void crawlingProcess(String url) {
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", driverLocation); // Local

        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-popup-blocking");       //팝업안띄움
//        options.addArguments("--disable-gpu");            //gpu 비활성화
//        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
//        options.addArguments("--headless");
        options.addArguments("--no-sandbox"); // linux 용
        options.addArguments("--disable-dev-shm-usage"); //linux 용

        driver = new ChromeDriver(options);
        js = (JavascriptExecutor) driver;

        try {
            getDataList(url);
        } catch (InterruptedException e) {
            System.out.println("**************************에러 발생 *************************");
            e.printStackTrace();
        }
        System.out.println("driver 종료");
//        driver.quit();     //브라우저 닫기

    }

    private List<String> getDataList(String url) throws InterruptedException {
        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(5000); //브라우저 로딩될때까지 잠시 기다린다.

        // 애견동반카페 버튼 클릭
        WebElement button = driver.findElement(By.xpath("//*[@id=\"container\"]/shrinkable-layout/div/app" +
                "-base/bubble-filter/bubble-filter-list/div/ul/li[1]/button"));
        js.executeScript("arguments[0].click();", button);
        Thread.sleep(2000);

        // SCROLL IFRAME 이동
        driver.switchTo().frame(driver.findElement(By.cssSelector("#searchIframe")));
        Thread.sleep(2000);

        int page = 0;
        while (true) {
            page++;
            log.info("page :{}", page);

            // 스크롤 맨 하단으로 이동
            for (int i = 0; i < 10; i++) {
                WebElement scroll = driver.findElement(By.cssSelector("#_pcmap_list_scroll_container"));
                js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight", scroll);

                Thread.sleep(500);
            }

            List<WebElement> elements = driver.findElements(By.cssSelector(".place_bluelink"));
            log.info("place count : {}", elements.size());

            for (WebElement element : elements) {
                NaverPlace np = new NaverPlace();
                log.info("place Name : {}", element.getText());
                np.setPlaceName(element.getText());
                // 상세 페이지 이동을 위해 PLACE NAME 클릭
                js.executeScript("arguments[0].click();", element);Thread.sleep(2000);

                // 상세 페이지 IFRAME으로 이동
                driver.switchTo().parentFrame();Thread.sleep(100); // 기존 IFRAME으로 이동
                driver.switchTo().frame(driver.findElement(By.cssSelector("#entryIframe")));
                WebElement address = driver.findElement(By.cssSelector(".LDgIH")); // 주소 Class
                WebElement category = driver.findElement(By.cssSelector(".DJJvD")); // 주소 Class
                log.info("address : {}", address.getText());
                np.setAddress(address.getText());
                log.info("category : {}", category.getText());
                np.setCategory(category.getText());

                // 사진 버튼
                List<WebElement> photoButtonList = driver.findElements(By.cssSelector(".veBoZ")); // Class
                for (WebElement photoButton : photoButtonList)
                    if (photoButton.getText().equals("사진")) {
                        js.executeScript("arguments[0].click();", photoButton);
                        break;
                    }
                Thread.sleep(2000);

                // 업체 사진 버튼
                List<WebElement> photoButton2List = driver.findElements(By.cssSelector(".BSSHM")); // Class
                for (WebElement photoButton : photoButton2List)
                    if (photoButton.getText().equals("업체사진")) {
                        js.executeScript("arguments[0].click();", photoButton);
                        break;
                    }

                Thread.sleep(2000);

                List<WebElement> photoUrls = driver.findElements(By.cssSelector(".wzrbN a.place_thumb img"));
                log.info("photoUrl Count : {}", photoUrls.size());
                List<String> photos = photoUrls.stream().map(photo -> photo.getAttribute("src")).collect(Collectors.toList());

                log.info("photos : {}", photos);
                np.setPhotos(photos);

                log.info("return np : {}", np);

                naverPlaceService.register(np);

                // SCROLL IFRAME 이동
                driver.switchTo().parentFrame();Thread.sleep(100);  // 기존 아이프레임으로 이동
                driver.switchTo().frame(driver.findElement(By.cssSelector("#searchIframe")));Thread.sleep(500);
            }

            List<WebElement> isUnusedBtn = driver.findElements(By.cssSelector(".Y89AQ"));
            if (page > 1 && isUnusedBtn.size() == 1) break; // 마지막 페이지

            // 다음 페이지 이동
            List<WebElement> nextBtn = driver.findElements(By.cssSelector(".eUTV2"));
            js.executeScript("arguments[0].click();", nextBtn.get(1));

            Thread.sleep(2000);
        }

            return null;
        }
}
