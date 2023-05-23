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
public class GeoService {
    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private static final String CLIENT_ID = "a8lt0yd9uy";
    private static final String CLIENT_SECRET = "P1WuQqH2d7rAnbWraxGwgDjPVvayuFwhV0RQAXtR";

    private final CodeService codeService;

    public Location getGeoData(String address) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

        String requestURL = API_URL + "?query=" + address;

        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        Location location = new Location();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            JsonNode addressesNode = jsonNode.get("addresses").get(0);
            JsonNode addressElementsNode = addressesNode.get("addressElements");

            location.setLatitude(Optional.ofNullable(addressesNode.get("y")).map(JsonNode::asText).orElse(""));
            location.setLongitude(Optional.ofNullable(addressesNode.get("x")).map(JsonNode::asText).orElse(""));
            location.setRoadAddress(Optional.ofNullable(addressesNode.get("roadAddress")).map(JsonNode::asText).orElse(""));
            location.setJibunAddress(Optional.ofNullable(addressesNode.get("jibunAddress")).map(JsonNode::asText).orElse(""));
            location.setSIGUGUN(Optional.ofNullable(addressElementsNode.get(1)).map(node -> node.get("longName")).map(JsonNode::asText).orElse(""));
            location.setSIDO(Optional.ofNullable(addressElementsNode.get(0)).map(node -> node.get("longName")).map(JsonNode::asText).orElse(""));

            // SET GEOCODE
            Code code = codeService.getGeoCodeByLocation(location);
            location.setGeoCode(code);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return location;
    }
}