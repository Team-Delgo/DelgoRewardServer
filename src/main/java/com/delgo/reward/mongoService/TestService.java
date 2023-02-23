package com.delgo.reward.mongoService;

import com.delgo.reward.mongoDomain.Test;
import com.delgo.reward.mongoRepository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private static final String CONTENT = "TEST CONTENT";
    private static final String UPDATE_CONTENT = "TEST UPDATE CONTENT";
    private String id;

    public void createTest(){
        System.out.println("[TestService] Start createTest");
        Test test = Test.builder()
                .content(CONTENT)
                .build();
        Test newData = testRepository.save(test);
        System.out.println("[TestService] New Data\n Id: " + newData.getId() + "\nContent: " + newData.getContent());
        this.id = newData.getId();
        System.out.println("[TestService] End createTest");

    }

    public void updateTest(){
        System.out.println("[TestService] Start updateTest");
        Test test = testRepository.findTestById(id).get();
        System.out.println("[TestService] Old Data\n Id: " + test.getId() + "\nContent: " + test.getContent());

        Test newData = testRepository.save(test.setContent(UPDATE_CONTENT));
        System.out.println("[TestService] New Data\n Id: " + newData.getId() + "\nContent: " + newData.getContent());
        System.out.println("[TestService] End updateTest");

    }

    public void readTest(){
        System.out.println("[TestService] Start readTest");
        Test test = testRepository.findTestById(id).get();
        System.out.println("[TestService] Data\n Id: " + test.getId() + "\nContent: " + test.getContent());
        System.out.println("[TestService] End readTest");
    }

    public void readAllTest(){
        System.out.println("[TestService] Start readAllTest");
        List<Test> testList = testRepository.findAll();
        for (Test t: testList) {
            System.out.println("[TestService] Data\n Id: " + t.getId() + "\nContent: " + t.getContent());
        }
        System.out.println("[TestService] End readAllTest");
    }

    public void deleteTest(){
        System.out.println("[TestService] Start deleteTest");
        testRepository.deleteById(id);
        System.out.println("[TestService] End deleteTest");
    }

    public void deleteAllTest(){
        System.out.println("[TestService] Start deleteAllTest");
        testRepository.deleteAll();
        System.out.println("[TestService] End deleteAllTest");
    }
}
