package com.delgo.reward.comm.ncp.greeneye;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class GreenEyeServiceTest {
    @Autowired
    GreenEyeService greenEyeService;

    @Test
    void isCorrect() {
        // given
        String url = "https://kr.object.ncloudstorage.com/reward-mungple/CA0002/%EC%86%A1%ED%8C%8C%EA%B5%AC/%EC%9C%88%EC%8D%B8%EB%A6%AC%EC%BB%A4%ED%94%BC.png";

        // when
        boolean result = greenEyeService.isCorrect(url);

        // then
        assertThat(result).isTrue();
    }
}