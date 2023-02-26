package com.delgo.reward;


import com.delgo.reward.comm.oauth.KakaoService;
import com.delgo.reward.comm.oauth.NaverService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OAuthTest {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private NaverService naverService;

    @Test
    public void KakaoLogin() throws Exception {
        String code = "_tnQ9x8NoFeh6E8_K3Q5Q5pWRbBoD2qQNO1LX4B_zbQnqBM1Yjl6OO9UzfZCQww0LJt_BQo9dRkAAAGEWwjeZQ";
        String token = kakaoService.getKakaoAccessToken(code);
        kakaoService.createKakaoUser(token);
    }

    @Test
    public void KakaoLogout() throws Exception {
        String id = "2371231033";
        kakaoService.logout(id);
    }

    @Test
    public void NaverLogin() throws Exception {
//       https://www.delgo.pet/oauth/callback/naver?code=GWx84ipdno3k59z44N&state=9kgsGTfH4j7IyAkg
        String state = "9kgsGTfH4j7IyAkg";
        String code = "GWx84ipdno3k59z44N";

        String token = naverService.getNaverAccessToken(state, code);

        naverService.createNaverUser(token);
    }

    @Test
    public void NaverLogOut() throws Exception {
       // "access_token":"AAAAOvSSVipN25hL4qLU13pIpDsZbgchc6H27vkyDvzLwNLyBMhzzMobL3wcWS0Mqp77VYCtE20jeCVkoo4CpgtdY70"
        String accessToken = "AAAANX7wskGdP1QG7O5fneV8sG28_OekyMDMQpb3SgZfglw1tRtwWJ4ZYGz58NUisqYVSxm1HveJ4z6oR6_cLn-K8esx";
        naverService.deleteNaverUser(accessToken);

    }
}
