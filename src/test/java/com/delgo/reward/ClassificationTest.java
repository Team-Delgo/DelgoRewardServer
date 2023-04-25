package com.delgo.reward;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClassificationTest {
    @Autowired
    private CertService certService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private ClassificationRepository classificationRepository;

    @Test
    public void classificationByCertificationTest() throws IOException, ParseException {
        // 시간 재는 코드 시작
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

        // 시간 재는 코드 끝
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        System.out.println("코드 실행 시간 (s): " + stopWatch.getTotalTimeSeconds());
    }

    @Test
    public void classificationTest() {
        final String DIR = "src/main/resources/classification_data_set/classification_category.json";

        JSONParser jsonParser = new JSONParser();
        Reader reader = null;
        try {
            reader = new FileReader(DIR);
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

        for (Certification certification : certificationList) {
            Classification classification = classificationRepository.save(classificationService.classificationCert(certification, categoryCodeList, categoryMap, classificationCriteriaMap));

            System.out.println(classification.getId());
            System.out.println(classification.getCategory());
            System.out.println(classification.getSido());
            System.out.println(classification.getSigugun());
            System.out.println(classification.getDong());

            classificationRepository.deleteById(classification.getId());
        }

    }
    @Test
    public void TEST_RESOURCE(){
        String CATEGORY_CLASSIFICATION_DATA_SET_DIR = "classification_data_set/category_test.json";

        ClassPathResource classPathResource = new ClassPathResource(CATEGORY_CLASSIFICATION_DATA_SET_DIR);
        JSONParser jsonParser = new JSONParser();
        Reader reader = null;

        try {
            reader = new FileReader(classPathResource.getFile());
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

    }

    @Test
    public void TEST_GET_CLASSIFICATION(){
        List<Classification> classificationList = classificationRepository.findAll();
        List<Certification> certificationList = new ArrayList<>();

        for(Classification classification: classificationList){
            certificationList.add(classification.getCertification());
        }

        for(Certification certification: certificationList){
            System.out.println(classificationRepository.findClassificationByCertification_CertificationId(certification).get().getUser());
        }

    }
}
