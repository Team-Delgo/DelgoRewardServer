package com.delgo.reward;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.mongoService.ClassificationService;
import com.delgo.reward.service.CertService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassificationTest {
    @Autowired
    private CertService certService;
    @Autowired
    private ClassificationService classificationService;

    @Test
    public void classificationByCertificationTest() throws IOException, ParseException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        JSONParser jsonParser = new JSONParser();
        Reader reader = new FileReader("src/test/resources/classification_data_set/category_test.json");

        JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);
        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (Object obj : jsonArray) {
            jsonObjectList.add((JSONObject) obj);
        }

        List<String> categoryCodeList = new ArrayList<>();
        Map<String, String> categoryMap = new HashMap<>();
        Map<String, List<String>> classificationCriteriaMap = new HashMap<>();

        for (JSONObject jsonObject : jsonObjectList) {
            String categoryCode = (String) jsonObject.get("category_code");
            categoryCodeList.add(categoryCode);
            categoryMap.put(categoryCode, (String) jsonObject.get("category_name"));
            classificationCriteriaMap.put(categoryCode, (List<String>) jsonObject.get("classification"));
        }

        String text = "오늘도 강아지와 산책! 건강한 하루를 보내는 중!";
        List<String> outputCategoryCodeList = new ArrayList<>();

        for (String categoryCode : categoryCodeList) {
            List<String> classificationCriteriaList = classificationCriteriaMap.get(categoryCode);
            for (String classificationCriteria : classificationCriteriaList) {
                if (text.contains(classificationCriteria)) {
                    outputCategoryCodeList.add(categoryCode);
                    break;
                }
            }
        }
        for (String outputCategoryCode : outputCategoryCodeList) {
            System.out.println("outputCategoryCode result: " + outputCategoryCode);
            System.out.println("outputCategoryCode categoryName: " + categoryMap.get(outputCategoryCode));
        }

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch.getTotalTimeSeconds());
    }

   @Test
   public void classificationTest(){
       JSONParser jsonParser = new JSONParser();
       Reader reader = null;
       try {
           reader = new FileReader("src/main/resources/classification_data_set/classification_category.json");
       } catch (Exception e) {
           throw new NullPointerException("NOT FOUND FILE");
       }

       JSONArray jsonArray = null;
       try {
           jsonArray = (JSONArray) jsonParser.parse(reader);
       } catch (Exception e) {
           throw new NullPointerException("NOT FOUND DATA");
       }

       List<JSONObject> jsonObjectList = new ArrayList<>();
       for (Object obj : jsonArray) {
           jsonObjectList.add((JSONObject) obj);
       }

       List<String> categoryCodeList = new ArrayList<>();
       Map<String, String> categoryMap = new HashMap<>();
       Map<String, List<String>> classificationCriteriaMap = new HashMap<>();

       for (JSONObject jsonObject : jsonObjectList) {
           String categoryCode = (String) jsonObject.get("category_code");
           categoryCodeList.add(categoryCode);
           categoryMap.put(categoryCode, (String) jsonObject.get("category_name"));
           classificationCriteriaMap.put(categoryCode, (List<String>) jsonObject.get("classification"));
       }

       List<Certification> certificationList = certService.getCertByDate(LocalDate.of(2023, 2, 14));

       for(Certification certification: certificationList){
           System.out.println(classificationService.classificationCert(certification, categoryCodeList, categoryMap, classificationCriteriaMap).getId());;
       }

   }
}
