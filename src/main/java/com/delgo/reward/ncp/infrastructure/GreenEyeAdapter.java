package com.delgo.reward.ncp.infrastructure;


import com.delgo.reward.ncp.domain.GreenEyeRequest;
import com.delgo.reward.ncp.domain.GreenEyeResponse;
import com.delgo.reward.ncp.service.port.GreenEyePort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Slf4j
@Component
public class GreenEyeAdapter implements GreenEyePort {
    private static final String API_URL = "https://clovagreeneye.apigw.ntruss.com/custom/v1/85/ca9aa01abc04c55c6f50193b55d1e8f49c5e49a01d954e3ff25959c0b9404b72/predict";
    private static final String SECRET_KEY = "RlpQTkxqbGtQeUNESkhjdW1Ma1VDUllmVllBUnVaa3o=";

    @Override
    public boolean isCorrect(String url) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-GREEN-EYE-SECRET", SECRET_KEY);

        List<GreenEyeRequest.Image> images = List.of(new GreenEyeRequest.Image("image", url));
        GreenEyeRequest request = GreenEyeRequest.create(images);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            GreenEyeResponse response = GreenEyeResponse.fromJson(jsonNode);

            boolean isCorrect = checkScore(response.getPorn(), response.getAdult());
            log.info("response : {}", response);
            log.info("isCorrect : {}", isCorrect);

            return isCorrect;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkScore(double porn, double adult) {
        return porn < 0.1 && adult < 0.1;
    }
}