package com.delgo.reward.service.crawling;

import com.delgo.reward.domain.Code;
import com.delgo.reward.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GetGeoCodeCrawlingService {

    private final CodeService codeService;
    @Value("${config.driverLocation}")
    String driverLocation;
    private WebDriver driver;

    public void crawlingProcess(String url) {
        System.setProperty("webdriver.chrome.whitelistedIps", "");
        System.setProperty("webdriver.chrome.driver", driverLocation); // Local

        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-popup-blocking");       //팝업안띄움
//        options.addArguments("--disable-gpu");            //gpu 비활성화
//        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
//        options.addArguments("--headless");
//        options.addArguments("--no-sandbox"); // linux 용
//        options.addArguments("--disable-dev-shm-usage"); //linux 용

        driver = new ChromeDriver(options);

        try {
            getGeoCodeList(url);
        } catch (InterruptedException e) {
            System.out.println("**************************에러 발생 *************************");
            e.printStackTrace();
        }
        System.out.println("driver 종료");
        driver.quit();     //브라우저 닫기

    }

    private List<String> getGeoCodeList(String url) throws InterruptedException {
        driver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(5000); //브라우저 로딩될때까지 잠시 기다린다.

        List<Code> codeList = getGeoCode();
        codeService.registerCodeList(codeList);
        return null;
    }

    // PlacePhoto 조회
    private List<Code> getGeoCode() {
        List<Code> codeList = new ArrayList<>();
        List<WebElement> elements = driver.findElements(By.cssSelector("tbody tr"));
        elements.forEach(element -> {
            if (!element.getText().equals("")) {
                String[] text_arr = element.getText().split(" ");
                String isKorea = text_arr[0].substring(0, 1);
                if (isKorea.equals("1")) {
                    if (text_arr.length == 4)
                        codeList.add(Code.builder()
                                .code(text_arr[2])
                                .pCode(text_arr[3])
                                .codeName(text_arr[1])
                                .codeDesc("")
                                .registDt(LocalDateTime.now())
                                .build());
                    else
                        codeList.add(Code.builder()
                                .code(text_arr[3])
                                .pCode(text_arr[4])
                                .codeName(text_arr[1] + " " + text_arr[2])
                                .codeDesc("")
                                .registDt(LocalDateTime.now())
                                .build());
                }
            }
        });

        return codeList;
    }
}
