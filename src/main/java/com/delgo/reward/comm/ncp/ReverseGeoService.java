package com.delgo.reward.comm.ncp;


import com.delgo.reward.domain.code.Code;
import com.delgo.reward.domain.common.Location;
import com.delgo.reward.service.CodeService;
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

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReverseGeoService {
    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
    private static final String CLIENT_ID = "a8lt0yd9uy";
    private static final String CLIENT_SECRET = "P1WuQqH2d7rAnbWraxGwgDjPVvayuFwhV0RQAXtR";

    private final CodeService codeService;

    public Location getReverseGeoData(Location location) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

        String requestURL = API_URL + "?coords=" + location.getCoordinate() + "&output=json";

        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            JsonNode resultsNode = jsonNode.get("results").get(0);
            JsonNode regionNode = resultsNode.get("region");

            location.setSIDO(Optional.ofNullable(regionNode.get("area1")).map(node -> node.get("name")).map(JsonNode::asText).orElse(""));
            location.setSIGUGUN(Optional.ofNullable(regionNode.get("area2")).map(node -> node.get("name")).map(JsonNode::asText).orElse(""));
            location.setDONG(Optional.ofNullable(regionNode.get("area3")).map(node -> node.get("name")).map(JsonNode::asText).orElse(""));

            // SET GEOCODE
            Code code = codeService.getGeoCodeByLocation(location);
            location.setGeoCode(code);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return location;
    }
}