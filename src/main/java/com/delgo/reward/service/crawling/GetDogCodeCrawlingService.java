//package com.delgo.reward.service.crawling;
//
//import com.delgo.reward.domain.code.Code;
//import com.delgo.reward.common.service.CodeService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//public class GetDogCodeCrawlingService {
//
//    private final CodeService codeService;
//    @Value("${config.driverLocation}")
//    String driverLocation;
//    private WebDriver driver;
//
//    public void crawlingProcess(String url) {
//        System.setProperty("webdriver.chrome.whitelistedIps", "");
//        System.setProperty("webdriver.chrome.driver", driverLocation); // Local
//
//        //크롬 드라이버 셋팅 (드라이버 설치한 경로 입력)
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--disable-popup-blocking");       //팝업안띄움
//        options.addArguments("--disable-gpu");            //gpu 비활성화
//        options.addArguments("--blink-settings=imagesEnabled=false"); //이미지 다운 안받음
//        options.addArguments("--headless");
//        options.addArguments("--no-sandbox"); // linux 용
//        options.addArguments("--disable-dev-shm-usage"); //linux 용
//
//        driver = new ChromeDriver(options);
//
//        try {
//            getGeoCodeList(url);
//        } catch (InterruptedException e) {
//            System.out.println("**************************에러 발생 *************************");
//            e.printStackTrace();
//        }
//        System.out.println("driver 종료");
//        driver.quit();     //브라우저 닫기
//
//    }
//
//    private List<String> getGeoCodeList(String url) throws InterruptedException {
//        driver.get(url);    //브라우저에서 url로 이동한다.
//        Thread.sleep(5000); //브라우저 로딩될때까지 잠시 기다린다.
//        List<Code> dogList = getDogList();
//        codeService.registerCodeList(dogList);
//
//        return null;
//    }
//
//    // PlacePhoto 조회
//    private List<Code> getDogList() {
//        List<Code> codeList = new ArrayList<>();
//        List<WebElement> elements = driver.findElements(By.cssSelector("ul li"));
//
////        int num = 12345;
////        int length = (int)(Math.log10(num)+1);
//
//        for(int num = 1; num < elements.size(); num++){
//            String code_code = "";
//            int length = (int)(Math.log10(num)+1);
//            switch (length){
//                case 1: code_code = "DT000" + num; break;
//                case 2: code_code = "DT00" + num; break;
//                case 3: code_code = "DT0" + num; break;
//                case 4: code_code = "DT" + num; break;
//            }
//            if (!elements.get(num).getText().equals("")) {
//                String text = elements.get(num).getText();
//                log.info("text :{} code: {}", text, code_code);
//
//                Code code = Code.builder()
//                        .code(code_code)
//                        .pCode(code_code)
//                        .codeName(text)
//                        .codeDesc(text)
//                        .type("dog")
//                        .build();
//                codeList.add(code);
//            }
//        }
//
//        return codeList;
//    }
//}
