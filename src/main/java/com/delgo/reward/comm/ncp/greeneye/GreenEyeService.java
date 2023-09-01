package com.delgo.reward.comm.ncp.greeneye;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class GreenEyeService {
    private static final String API_URL = "https://clovagreeneye.apigw.ntruss.com/custom/v1/85/ca9aa01abc04c55c6f50193b55d1e8f49c5e49a01d954e3ff25959c0b9404b72/predict";
    private static final String SECRET_KEY = "RlpQTkxqbGtQeUNESkhjdW1Ma1VDUllmVllBUnVaa3o=";

    public boolean checkHarmfulPhoto(String url) throws JsonProcessingException {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-GREEN-EYE-SECRET", SECRET_KEY);

        // Create an instance of dto
        List<GreenEyeReqDTO.Image> images = new ArrayList<>();
        images.add(new GreenEyeReqDTO.Image("image", url));
        GreenEyeReqDTO reqDTO = GreenEyeReqDTO.builder()
                .version("V1")
                .requestId(String.valueOf(UUID.randomUUID()))
                .images(images)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(reqDTO), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            GreenEyeResDTO greenEyeResDTO = GreenEyeResDTO.builder()
                    .adult(jsonNode.at("/images/0/result/adult/confidence").asDouble())
                    .normal(jsonNode.at("/images/0/result/normal/confidence").asDouble())
                    .porn(jsonNode.at("/images/0/result/porn/confidence").asDouble())
                    .sexy(jsonNode.at("/images/0/result/sexy/confidence").asDouble())
                    .build();

            boolean isCorrect = greenEyeResDTO.getPorn() < 0.1 && greenEyeResDTO.getAdult() < 0.1;
            log.info("greenEyeResDTO : {}", greenEyeResDTO);
            log.info("isCorrect : {}", isCorrect);

            return isCorrect;
        } catch (Exception e) {
            return false;
        }
    }
}