package com.delgo.reward.service;

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
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class FigmaService {

    private final String API_URL = "https://api.figma.com/v1/";
    private final String figmaToken = "figd_r19ArRmULsFDOcl1Mim1B7zpphHYgqYM-YT84yfI";
    private final String figmaFileKey = "Lrcjf0B8Up9Zp2fdHK2EnN";

    // https://api.figma.com/v1/files/yzrVwMDiG6uU8LM57RUfN0/nodes?ids=5:39
    public Map<String, ArrayList<String>> getComponent(String nodeId){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-FIGMA-TOKEN", figmaToken);

        String requestURL = API_URL + "files/" + figmaFileKey +"/nodes?ids=" + nodeId;

        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

        Map<String, ArrayList<String>> ids_map = Map.of(
                "photo", new ArrayList<>(),
                "menu", new ArrayList<>(),
                "menu_board", new ArrayList<>(),
                "price", new ArrayList<>(),
                "dog", new ArrayList<>()
        );

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
            JsonNode childrNodes = rootNode.get("nodes").get(nodeId).get("document").get("children");

            for (JsonNode childNode : childrNodes) {
                String name = childNode.get("name").asText();
                String type = checkPhotoType(name);
                ids_map.get(type).add(childNode.get("id").asText());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("photo ids : {}", ids_map.get("photo"));
        log.info("menu ids : {}", ids_map.get("menu"));
        log.info("menu board ids : {}", ids_map.get("menu_board"));
        log.info("price ids : {}", ids_map.get("price"));
        log.info("dog ids : {}", ids_map.get("dog"));
        return ids_map;
    }

    // https://api.figma.com/v1/images/Lrcjf0B8Up9Zp2fdHK2EnN?ids=4908-43405&format=png
    public List<String> getImage(List<String> ids){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-FIGMA-TOKEN", figmaToken);

        String requestURL = API_URL + "images/" + figmaFileKey +"?format=png&ids=" + String.join(",", ids);

        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> images = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
            JsonNode imageNodes = rootNode.get("images");

            for (JsonNode imageNode : imageNodes) {
                images.add(imageNode.asText());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for(String img: images)
            log.info("img :{}", img);
        return images;
    }

    public String checkPhotoType(String text) {
        String[] split = text.split("_");

        if (split.length == 4) {
            return "photo";
        } else if (split.length == 6) {
            return "menu_board";
        } else {
            log.info("text: {}", text);
            String type = split[3];
            return switch (type) {
                case "menu" -> "menu";
                case "price" -> "price";
                case "dog" -> "dog";
                default -> "";
            };
        }
    }
}
