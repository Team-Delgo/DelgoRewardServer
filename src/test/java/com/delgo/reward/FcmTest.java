package com.delgo.reward;

import com.delgo.reward.comm.fcm.FcmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FcmTest {

    @Autowired
    private FcmService fcmService;

    @Test
    public void test() throws IOException {
        int userId = 255;
        String username = "tester";
        String content = "test comment";
        fcmService.commentPush(userId, username, content);

    }
}
