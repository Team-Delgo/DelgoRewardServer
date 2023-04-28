package com.delgo.reward.mongoService;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.domain.pet.Pet;
import com.delgo.reward.domain.user.CategoryCount;
import com.delgo.reward.domain.user.User;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import com.delgo.reward.record.certification.ModifyCertRecord;
import com.delgo.reward.service.CertService;
import com.delgo.reward.service.PetService;
import com.delgo.reward.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.Reader;
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
    private final CertService certService;
    private final ClassificationRepository classificationRepository;

    private final static String CATEGORY_CLASSIFICATION_DATA_SET_DIR = "classification_data_set/classification_category.json";
    private final static String CATEGORY_CODE = "CA9999";
    private final static String CATEGORY_NAME = "기타";

    public Classification runClassification(Certification certification) {
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

        return classificationRepository.save(classificationCert(certification, categoryCodeList, categoryMap, classificationCriteriaMap));
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

        return new Classification().toEntity(user, pet, certification, outputCategory, sido, sigugun, dong);
    }

    public void deleteClassificationWhenModifyCert(ModifyCertRecord modifyCertRecord){
        Certification certification = certService.getCertById(modifyCertRecord.certificationId());
        Classification classification = classificationRepository.findClassificationByCertification_CertificationId(modifyCertRecord.certificationId()).get();

        CategoryCount categoryCount = userService.getCategoryCountByUserId(certification.getUserId());

        for(String categoryCode: classification.getCategory().keySet()){
            userService.categoryCountSave(categoryCount.minusOne(categoryCode));
        }

        classificationRepository.delete(classification);
    }
}
