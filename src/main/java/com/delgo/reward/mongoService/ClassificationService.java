package com.delgo.reward.mongoService;

import com.delgo.reward.comm.code.CategoryCode;
import com.delgo.reward.cert.domain.Certification;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationRepository classificationRepository;

    private final static String CATEGORY_CLASSIFICATION_DATA_SET_DIR = "classification_data_set/classification_category.json";

    public Map<CategoryCode, Integer> getCategoryCountMapByUserId(Integer userId) {
        Map<CategoryCode, Integer> activityMapByCategoryCode = new HashMap<>();
        List<Classification> classificationList = classificationRepository.findAllByUser_UserId(userId);
        for (Classification classification : classificationList) {
            for (String key : classification.getCategory().keySet()) {
                CategoryCode categoryCode = CategoryCode.valueOf(key);
                int currentCount = activityMapByCategoryCode.getOrDefault(categoryCode, 0);
                activityMapByCategoryCode.put(categoryCode, currentCount + 1);
            }
        }
        return activityMapByCategoryCode;
    }

    public Classification create(Certification certification) {
        ClassPathResource categoryClassificationResource = new ClassPathResource(CATEGORY_CLASSIFICATION_DATA_SET_DIR);

        JSONParser jsonParser = new JSONParser();
        Reader reader = null;
        try {
            reader = new FileReader(categoryClassificationResource.getFile());
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

        return classificationRepository.save(classifyCert(certification, categoryCodeList, categoryMap, classificationCriteriaMap));
    }

    public Classification classifyCert(Certification certification, List<String> categoryCodeList, Map<String, String> categoryMap, Map<String, List<String>> classificationCriteriaMap) {
        String text = certification.getDescription();
        String defaultCategory = certification.getMungpleId() != 0 ? certification.getCategoryCode().getCode() : CategoryCode.CA9999.getCode();

        List<String> outputCategoryCodeList = new ArrayList<>();
        for (String categoryCode : categoryCodeList) {
            if(defaultCategory.equalsIgnoreCase(categoryCode)){
                outputCategoryCodeList.add(categoryCode);
            } else {
                List<String> classificationCriteriaList = classificationCriteriaMap.get(categoryCode);
                for (String classificationCriteria : classificationCriteriaList) {
                    if (text.contains(classificationCriteria)) {
                        outputCategoryCodeList.add(categoryCode);
                        break;
                    }
                }
            }
        }

        Map<String, String> outputCategory = new HashMap<>();
        for (String outputCategoryCode : outputCategoryCodeList) {
            outputCategory.put(outputCategoryCode, categoryMap.get(outputCategoryCode));
        }


        String address[] = certification.getAddress().split(" ");
        String sido = null;
        String sigugun = null;
        String dong = null;

        if(address.length == 2){
            sido = address[0];
            sigugun = address[1];
        } else {
            sido = address[0];
            sigugun = address[1];
            dong = address[2];
        }

        return Classification.from(certification, outputCategory, sido, sigugun, dong);
    }

    public void delete(Certification certification){
        Optional<Classification> optional = classificationRepository.findClassificationByCertification_CertificationId(certification.getCertificationId());
        optional.ifPresent(classification -> classificationRepository.delete(classification));
    }
}
