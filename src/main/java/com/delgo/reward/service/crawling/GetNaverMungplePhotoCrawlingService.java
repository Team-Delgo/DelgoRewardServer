package com.delgo.reward.service.crawling;

import com.delgo.reward.domain.Mungple;
import com.delgo.reward.mongoRepository.MungpleDetailRepository;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.delgo.reward.repository.MungpleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GetNaverMungplePhotoCrawlingService {

    @Value("${config.driverLocation}")
    String driverLocation;
    JavascriptExecutor js;
    private WebDriver driver;

    private final MungpleDetailRepository mungpleDetailRepository;
    private final MongoMungpleService mongoMungpleService;
    private final MungpleRepository mungpleRepository;


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
//            getPhoto(url);
            getPhotoByMorePlaces(url);
        } catch (InterruptedException e) {
            System.out.println("**************************에러 발생 *************************");
            e.printStackTrace();
        }
        System.out.println("driver 종료");
//        driver.quit();     //브라우저 닫기

    }

    private void getPhoto(String url) throws InterruptedException {
        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(3000); //브라우저 로딩될때까지 잠시 기다린다.

        List<Mungple> mungples = mungpleRepository.findAllByIsActive(true);
        for(int m = 1 ; m < mungples.size(); m++) {
            log.info("{} mungple name : {}", m, mungples.get(m).getPlaceName());

            // 애견동반카페 버튼 클릭
            WebElement searchBox = driver.findElement(By.xpath("/html/body/app/layout/div[3]/div[2]/shrinkable-layout/div/app-base/search-input-box/div/div[1]/div/input"));
            searchBox.clear();
            searchBox.sendKeys(mungples.get(m).getPlaceName());
            searchBox.sendKeys(Keys.ENTER);
            Thread.sleep(2000);

            try {
                driver.switchTo().frame(driver.findElement(By.cssSelector("#entryIframe")));
                Thread.sleep(500);

                // [사진] 버튼
                List<WebElement> btn_bar = driver.findElements(By.xpath("/html/body/div[3]/div/div/div/div[5]/div/div/div/div/a"));
                int photoOrder = 0;
                for (int i = 0; i < btn_bar.size(); i++) {
                    if (btn_bar.get(i).getText().equals("사진"))
                        photoOrder = i + 1;
                }
                WebElement photoBtn = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[5]/div/div/div/div/a[" + photoOrder + "]/span"));
                js.executeScript("arguments[0].click();", photoBtn);
                Thread.sleep(2000);

                WebElement photo_store_Btn = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[7]/div[3]/div/div/div/div/span[1]/a/span[1]"));
                js.executeScript("arguments[0].click();", photo_store_Btn);
                Thread.sleep(2000);

                List<WebElement> photoUrls = driver.findElements(By.cssSelector(".wzrbN a img")); // 주소 Class
                List<String> photos = photoUrls.stream().map(photo -> photo.getAttribute("src")).toList();
                if (photos.size() >= 4) {
                    photos = photos.subList(0, 4);
                }
                for (String photo : photos)
                    log.info("photo : {}", photo);

                mongoMungpleService.modifyPhotoUrls(mungples.get(m).getMungpleId(),photos);

                try {
                    WebElement photo_menu_Btn = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[7]/div[3]/div/div/div/div/span[6]/a/span[1]"));
                    js.executeScript("arguments[0].click();", photo_menu_Btn);
                    Thread.sleep(2000);

                    List<WebElement> menuPhotoUrls = driver.findElements(By.cssSelector(".wzrbN a img")); // 주소 Class

                    List<String> menuPhotos = menuPhotoUrls.stream().map(photo -> photo.getAttribute("src")).toList();
                    log.info("menu photo : {}", menuPhotos.get(0));

                    mongoMungpleService.modifyMenuPhotoUrl(mungples.get(m).getMungpleId(), menuPhotos.get(0));
                } catch (NoSuchElementException e) {
                    log.info("menu photo 존재치 않음.");
                    // 대체 동작 또는 예외 처리 로직을 작성
                }


            } catch (Exception ignored) {
                log.info("같은 이름의 장소가 여러개 존재함.");
            }

            log.info("--------------------------------------------------------------------------------------");

            driver.switchTo().parentFrame();
            Thread.sleep(100); // 기존 IFRAME으로 이동
        }
    }

    // 검색 시 두개 이상의 장소가 나오는 멍플은 이걸로 크롤링
    private void getPhotoByMorePlaces(String url) throws InterruptedException {
        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(3000); //브라우저 로딩될때까지 잠시 기다린다.

        List<Mungple> mungples = mungpleRepository.findAllByIsActive(true);
        for (int m = 1; m < mungples.size(); m++) {

        log.info("{} mungple name : {}", m, mungples.get(m).getPlaceName());
        // 애견동반카페 버튼 클릭
        WebElement searchBox = driver.findElement(By.xpath("/html/body/app/layout/div[3]/div[2]/shrinkable-layout/div/app-base/search-input-box/div/div[1]/div/input"));
        searchBox.clear();
            searchBox.sendKeys(mungples.get(m).getPlaceName());
//        searchBox.sendKeys("브라운칩");
        searchBox.sendKeys(Keys.ENTER);
        Thread.sleep(2000);

        driver.switchTo().frame(driver.findElement(By.cssSelector("#searchIframe")));
        Thread.sleep(500);

        List<WebElement> place_list = driver.findElements(By.xpath("//*[@id=\"_pcmap_list_scroll_container\"]/ul/li/div[1]/div[2]/div/div/span/a/span[1]"));
        if (place_list.size() >= 2) {
            for (int i = 0; i < place_list.size(); i++) {
                log.info("place : {}", place_list.get(i).getText());
            }
            WebElement place = driver.findElement(By.xpath("/html/body/div[3]/div/div[2]/div[1]/ul/li[1]/div[1]/div[1]/a"));
            js.executeScript("arguments[0].click();", place);
            Thread.sleep(2000);

            driver.switchTo().parentFrame();
            Thread.sleep(100); // 기존 IFRAME으로 이동

            try {
                driver.switchTo().frame(driver.findElement(By.cssSelector("#entryIframe")));
                Thread.sleep(100);

                // [사진] 버튼
                List<WebElement> btn_bar = driver.findElements(By.xpath("/html/body/div[3]/div/div/div/div[5]/div/div/div/div/a"));
                int photoOrder = 0;
                for (int i = 0; i < btn_bar.size(); i++) {
                    if (btn_bar.get(i).getText().equals("사진"))
                        photoOrder = i + 1;
                }
                WebElement photoBtn = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[5]/div/div/div/div/a[" + photoOrder + "]/span"));
                js.executeScript("arguments[0].click();", photoBtn);
                Thread.sleep(2000);

                WebElement photo_store_Btn = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[7]/div[3]/div/div/div/div/span[1]/a/span[1]"));
                js.executeScript("arguments[0].click();", photo_store_Btn);
                Thread.sleep(2000);

                List<WebElement> photoUrls = driver.findElements(By.cssSelector(".wzrbN a img")); // 주소 Class
                List<String> photos = photoUrls.stream().map(photo -> photo.getAttribute("src")).toList();
                if (photos.size() >= 4) {
                    photos = photos.subList(0, 4);
                }
                for (String photo : photos)
                    log.info("photo : {}", photo);

                mongoMungpleService.modifyPhotoUrls(mungples.get(m).getMungpleId(),photos);

                try {
                    WebElement photo_menu_Btn = driver.findElement(By.xpath("/html/body/div[3]/div/div/div/div[7]/div[3]/div/div/div/div/span[6]/a/span[1]"));
                    js.executeScript("arguments[0].click();", photo_menu_Btn);
                    Thread.sleep(2000);

                    List<WebElement> menuPhotoUrls = driver.findElements(By.cssSelector(".wzrbN a img")); // 주소 Class

                    List<String> menuPhotos = menuPhotoUrls.stream().map(photo -> photo.getAttribute("src")).toList();
                    log.info("menu photo : {}", menuPhotos.get(0));

                    mongoMungpleService.modifyMenuPhotoUrl(mungples.get(m).getMungpleId(), menuPhotos.get(0));
                } catch (NoSuchElementException e) {
                    log.info("menu photo 존재치 않음.");
                    // 대체 동작 또는 예외 처리 로직을 작성
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
//                log.info("같은 이름의 장소가 여러개 존재함.");
            }
        }

            log.info("--------------------------------------------------------------------------------------");

            driver.switchTo().parentFrame();
            Thread.sleep(100); // 기존 IFRAME으로 이동
        }
    }
}
