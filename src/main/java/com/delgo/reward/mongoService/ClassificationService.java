package com.delgo.reward.mongoService;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final UserService userService;
    private final PetService petService;
    private final ClassificationRepository classificationRepository;

    private final static String CATEGORY_CODE = "CA9999";
    private final static String CATEGORY_NAME = "기타";

    public void runClassification(Certification certification) {
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

        classificationRepository.save(classificationCert(certification, categoryCodeList, categoryMap, classificationCriteriaMap));
    }


    public Classification classificationCert(Certification certification, List<String> categoryCodeList, Map<String, String> categoryMap, Map<String, List<String>> classificationCriteriaMap) {
        User user = userService.getUserById(certification.getUserId());
        Pet pet = petService.getPetByUserId(user.getUserId());
        String text = certification.getDescription();

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

        Map<String, String> outputCategory = new HashMap<>();
        for (String outputCategoryCode : outputCategoryCodeList) {
            outputCategory.put(outputCategoryCode, categoryMap.get(outputCategoryCode));
        }

        if (outputCategory.isEmpty()) {
            outputCategory.put(CATEGORY_CODE, CATEGORY_NAME);
        }

        return new Classification().toEntity(user, pet, certification, outputCategory);
    }


}
