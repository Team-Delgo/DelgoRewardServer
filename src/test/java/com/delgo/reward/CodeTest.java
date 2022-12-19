package com.delgo.reward;


import com.delgo.reward.domain.code.Code;
import com.delgo.reward.service.CodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeTest {

    @Autowired
    private CodeService codeService;

    @Test
    public void getGeoCodeFromDBTest() {
        //given
        String SIGUGUN = "송파구";

        //when
        Code code = new Code();

        System.out.println("geoCode : " + code.getCode());
        System.out.println("p_geoCode : " + code.getPCode());
        //then
        assertNotNull(code);
    }
}