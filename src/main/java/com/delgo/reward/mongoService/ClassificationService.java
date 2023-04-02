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


    public Classification classificationCert(Certification certification, List<String> categoryCodeList, Map<String, String> categoryMap, Map<String, List<String>> classificationCriteriaMap){
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

        if(outputCategory.isEmpty()){
            outputCategory.put(CATEGORY_CODE, CATEGORY_NAME);
        }

        return classificationRepository.save(new Classification().toEntity(user, pet, certification, outputCategory));
    }

}
