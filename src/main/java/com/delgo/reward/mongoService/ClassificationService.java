package com.delgo.reward.mongoService;

import com.delgo.reward.domain.certification.Certification;
import com.delgo.reward.mongoDomain.Classification;
import com.delgo.reward.mongoRepository.ClassificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {
    private final ClassificationRepository classificationRepository;

//    public Classification classificationByCertification(Certification certification) throws IOException, ParseException {
//        JSONParser jsonParser = new JSONParser();
//
//        Reader reader = new FileReader("resources/classification_data_set/category_test.json");
//        JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
//
//        String categoryCode = (String) jsonObject.get("category_code");
//        String categoryName = (String) jsonObject.get("category_name");
//
//        List<String> classificationCriteriaList = (List<String>) jsonObject.get("classification");
//
//
//    }

}
