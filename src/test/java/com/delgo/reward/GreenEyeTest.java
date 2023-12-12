package com.delgo.reward;

import com.delgo.reward.comm.ncp.greeneye.GreenEyeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@Slf4j
@SpringBootTest
public class GreenEyeTest {

    @Autowired
    private GreenEyeService greenEyeService;


    @Test
    public void check() {
        String url = "https://kr.object.ncloudstorage.com/test-certification/2072_cert_2.webp?2312120931044176";
        boolean result = greenEyeService.isCorrect(url);

        System.out.println("result = " + result);
    }
}