package com.delgo.reward;

import com.delgo.reward.mongoService.TestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTest {
    @Autowired
    private TestService testService;

    @Test
    public void 테스트(){
        testService.createTest();
        testService.readTest();
        testService.updateTest();
        testService.readAllTest();
        testService.deleteTest();
        testService.readAllTest();
        testService.deleteAllTest();
        testService.readAllTest();
    }
}
