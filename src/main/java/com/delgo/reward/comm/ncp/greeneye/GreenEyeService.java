package com.delgo.reward.comm.ncp.greeneye;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class GreenEyeService {
    @Value("${ncp.green-eye.url}")
    String API_URL;
    @Value("${ncp.green-eye.secret-key}")
    String SECRET_KEY;

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
            log.info("isCorrect : {}", isCorrect);

            return isCorrect;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private boolean checkScore(double porn, double adult) {
        return porn < 0.1 && adult < 0.1;
    }
}