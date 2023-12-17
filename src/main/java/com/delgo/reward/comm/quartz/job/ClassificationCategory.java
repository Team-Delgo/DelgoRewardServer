//package com.delgo.reward.comm.quartz.job;
//
//import com.delgo.reward.domain.certification.Certification;
//import com.delgo.reward.mongoService.ClassificationService;
//import com.delgo.reward.service.cert.CertService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.quartz.JobExecutionContext;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import java.io.FileReader;
//import java.io.Reader;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@RequiredArgsConstructor
//public class ClassificationCategory extends QuartzJobBean {
//    private final CertService certService;
//    private final ClassificationService classificationService;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) {
//        log.info(LocalTime.now() + ": ClassificationCategory Execute");
//
//        JSONParser jsonParser = new JSONParser();
//        Reader reader = null;
//        try {
//            reader = new FileReader("src/main/resources/classification_data_set/classification_category.json");
//        } catch (Exception e) {
//            throw new NullPointerException("NOT FOUND FILE");
//        }
//
//        JSONArray jsonArray = null;
//        try {
//            jsonArray = (JSONArray) jsonParser.parse(reader);
//        } catch (Exception e) {
//            throw new NullPointerException("NOT FOUND DATA");
//        }
//
//        List<JSONObject> jsonObjectList = new ArrayList<>();
//        for (Object obj : jsonArray) {
//            jsonObjectList.add((JSONObject) obj);
//        }
//
//        List<String> categoryCodeList = new ArrayList<>();
//        Map<String, String> categoryMap = new HashMap<>();
//        Map<String, List<String>> classificationCriteriaMap = new HashMap<>();
//
//        for (JSONObject jsonObject : jsonObjectList) {
//            String categoryCode = (String) jsonObject.get("category_code");
//            categoryCodeList.add(categoryCode);
//            categoryMap.put(categoryCode, (String) jsonObject.get("category_name"));
//            classificationCriteriaMap.put(categoryCode, (List<String>) jsonObject.get("classification"));
//        }
//
//        List<Certification> certificationList = certService.getCertListByDate(LocalDate.now());
//
//        for(Certification certification: certificationList){
//            classificationService.classificationCert(certification, categoryCodeList, categoryMap, classificationCriteriaMap);
//        }
//
//
//        log.info(LocalTime.now() + ": ClassificationCategory Exit");
//    }
//}
