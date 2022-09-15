package com.delgo.reward.comm.ncp;


import com.delgo.reward.domain.common.Location;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class GeoService {
    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private static final String CLIENT_ID = "a8lt0yd9uy";
    private static final String CLIENT_SECRET = "P1WuQqH2d7rAnbWraxGwgDjPVvayuFwhV0RQAXtR";


    public Location getGeoData(@RequestParam String address) {
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
            JsonNode latitude = jsonNode.get("addresses").get(0).get("x");
            JsonNode longitude = jsonNode.get("addresses").get(0).get("y");
            JsonNode roadAddress = jsonNode.get("addresses").get(0).get("roadAddress");
            JsonNode jibunAddress = jsonNode.get("addresses").get(0).get("jibunAddress");
            JsonNode distance = jsonNode.get("addresses").get(0).get("distance");
            JsonNode SIGUGUN = jsonNode.get("addresses").get(0).get("addressElements").get(1).get("longName");

            location.setLatitude(latitude.toString().replace("\"",""));
            location.setLongitude(longitude.toString().replace("\"",""));
            location.setRoadAddress(roadAddress.toString().replace("\"",""));
            location.setJibunAddress(jibunAddress.toString().replace("\"",""));
            location.setSIGUGUN(SIGUGUN.toString().replace("\"",""));

            System.out.println("latitude: " + latitude);
            System.out.println("longitude: " + longitude);
            System.out.println("roadAddress: " + roadAddress);
            System.out.println("jibunAddress: " + jibunAddress);
            System.out.println("distance: " + distance);
            System.out.println("SIGUGUN: " + SIGUGUN);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return location;
    }

    public Location getDistance(@RequestParam String address, String x, String y) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

        String requestURL = API_URL + "?query=" + address + "&coordinate=" + x +"," + y;

        HttpEntity entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);


        ObjectMapper objectMapper = new ObjectMapper();
        Location location = new Location();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            JsonNode latitude = jsonNode.get("addresses").get(0).get("x");
            JsonNode longitude = jsonNode.get("addresses").get(0).get("y");
            JsonNode distance = jsonNode.get("addresses").get(0).get("distance");
            JsonNode test = jsonNode.get("addresses").get(0);

            location.setLatitude(latitude.toString());
            location.setLongitude(longitude.toString());
            System.out.println("latitude: " + latitude);
            System.out.println("longitude: " + longitude);
            System.out.println("distance: " + distance);
            System.out.println("test: " + test);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return location;
    }
}