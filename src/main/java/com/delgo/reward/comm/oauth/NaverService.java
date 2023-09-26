package com.delgo.reward.comm.oauth;


import com.delgo.reward.comm.code.UserSocial;
import com.delgo.reward.dto.OAuthDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.function.Function;


@Slf4j
@Service
public class NaverService {
    private static final String CLIENT_ID = "lKCaP0liLFM9CZqBZ_4K";
    private static final String CLIENT_SECRET = "c5ewnvIv8O";

    public String getNaverAccessToken(String state, String code) {
        String accessToken = "";

        String API_URL = "https://nid.naver.com/oauth2.0/token";
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();

        String requestURL = API_URL
                + "?grant_type=authorization_code"
                + "&client_id=" + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET
                + "&code=" + code // 인증 코드
                + "&state=" + state; // 상태 토큰

        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            accessToken = jsonNode.get("access_token").toString().replace("\"","");

//            System.out.println("************************************************");
//            System.out.println("jsonNode: " + jsonNode);
//            System.out.println("accessToken: " + accessToken);
//            System.out.println("************************************************");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public OAuthDTO createNaverUser(String accessToken) {
        String API_URL = "https://openapi.naver.com/v1/nid/me";

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        OAuthDTO oAuthDTO = new OAuthDTO();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            String phoneNo = jsonNode.get("response").get("mobile").toString().replace("\"","");
            String email = jsonNode.get("response").get("email").toString().replace("\"","");
            String gender = jsonNode.get("response").get("gender").toString().replace("\"","");
            String birthyear = jsonNode.get("response").get("birthyear").toString().replace("\"","");

            Function<String, Integer> calculateAge = yearOfBirth -> {
                int currentYear = LocalDate.now().getYear();
                return currentYear - Integer.parseInt(yearOfBirth) + 1;
            };

            oAuthDTO.setPhoneNo(phoneNo);
            oAuthDTO.setEmail(email);
            oAuthDTO.setGender(gender);
            oAuthDTO.setAge(calculateAge.apply(birthyear));
            oAuthDTO.setUserSocial(UserSocial.N);

            System.out.println("************************************************");
            System.out.println("jsonNode: " + jsonNode);
            System.out.println("phoneNo: " + phoneNo);
            System.out.println("email: " + email);
            System.out.println("gender: " + gender);
            System.out.println("birthyear: " + birthyear);
            System.out.println("age: " + calculateAge.apply(birthyear));
            System.out.println("************************************************");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oAuthDTO;
    }

    public void deleteNaverUser(String accessToken) {
        // https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=jyvqXeaVOVmV&client_secret=527300A0_COq1_XV33cf&access_token=c8ceMEJisO4Se7uGCEYKK1p52L93bHXLnaoETis9YzjfnorlQwEisqemfpKHUq2gY&service_provider=NAVER
        String API_URL = "https://nid.naver.com/oauth2.0/token";
        String requestURL = API_URL
                + "?grant_type=delete"
                + "&client_id=" + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET
                + "&access_token=" + accessToken // 인증 코드
                + "&service_provider=NAVER"; // 상태 토큰

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());


            System.out.println("************************************************");
            System.out.println("jsonNode: " + jsonNode);
            System.out.println("************************************************");

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}