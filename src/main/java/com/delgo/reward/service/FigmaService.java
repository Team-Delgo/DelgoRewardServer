package com.delgo.reward.service;

import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.comm.ncp.storage.ObjectStorageService;
import com.delgo.reward.mongoDomain.mungple.MongoMungple;
import com.delgo.reward.mongoService.MongoMungpleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class FigmaService {
    private final PhotoService photoService;
    private final MongoMungpleService mongoMungpleService;
    private final ObjectStorageService objectStorageService;

    private final String API_URL = "https://api.figma.com/v1/";
    private final String figmaToken = "figd_r19ArRmULsFDOcl1Mim1B7zpphHYgqYM-YT84yfI";
    private final String figmaFileKey = "Lrcjf0B8Up9Zp2fdHK2EnN";

    @Value("${config.photoDir}")
    String DIR;

    public void uploadFigmaDataToNCP(String nodeId, int mungpleId) {
        try {
            // Figma 연동 Data 조회
            Map<String, String> imageIdMap = getImageIdFromFigma(nodeId);
            Map<String, String> imageUrlMap = getImageUrlFromFigma(imageIdMap);

            // Mungple 조회
            MongoMungple mongoMungple = mongoMungpleService.getMungpleByMungpleId(mungpleId);

            // typeListMap 초기화
            Map<String, ArrayList<String>> typeListMap = Map.of(
                    "thumbnail", new ArrayList<>(),
                    "menu", new ArrayList<>(),
                    "menu_board", new ArrayList<>(),
                    "price_tag", new ArrayList<>(),
                    "dog", new ArrayList<>()
            );

            // imageUrlMap을 Type에 맞게 각 List에 저장
            processImages(imageUrlMap, typeListMap);

            // typeList를 각 Fileds에 매치 후 저장
            mongoMungple.setFigmaPhotoData(typeListMap);
            mongoMungpleService.save(mongoMungple);

        } catch (Exception e){
            throw new RuntimeException("[uploadFigmaDataToNCP] mungpleId :"  + mungpleId);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-FIGMA-TOKEN", figmaToken);
        return headers;
    }

    private RestTemplate createRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        return new RestTemplate(httpRequestFactory);
    }

    private Map<String, String> getImageIdFromFigma(String nodeId){
        RestTemplate restTemplate = createRestTemplate();
        HttpHeaders headers = createHeaders();

        String requestURL = API_URL + "files/" + figmaFileKey +"/nodes?ids=" + nodeId;
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Map<String, String> imageIdMap = new HashMap<>();
        try {
            JsonNode rootNode = new ObjectMapper().readTree(responseEntity.getBody());
            JsonNode childrNodes = rootNode.get("nodes").get(nodeId).get("document").get("children");

            for (JsonNode childNode : childrNodes) {
                String imageId = childNode.get("id").asText(); // 4935:43532
                String fileName = childNode.get("name").asText(); // 강동구_애견동반식당_담금_thumbnail_5// 강동구_애견동반식당_담금_thumbnail_5
                imageIdMap.put(imageId, fileName);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("[getImageIdFromFigma] :"  + nodeId);
        }

        return imageIdMap;
    }

    private Map<String,String> getImageUrlFromFigma(Map<String,String> imageIdMap){
        RestTemplate restTemplate = createRestTemplate();
        HttpHeaders headers = createHeaders();

        String requestURL = API_URL + "images/" + figmaFileKey +"?format=png&ids=" + String.join(",", imageIdMap.keySet());
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Map<String,String> imageUrlMap = new HashMap<>();
        try {
            JsonNode rootNode = new ObjectMapper().readTree(responseEntity.getBody());
            JsonNode imageNodes = rootNode.get("images");
            Iterator<Map.Entry<String, JsonNode>> fields = imageNodes.fields();
            while (fields.hasNext()) { // Iterator<Map.Entry<String, JsonNode>>
                Map.Entry<String, JsonNode> field = fields.next();
                String imageId = field.getKey(); // ex) 4931:43548
                String imageUrl = field.getValue().asText(); // ex) https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/8ab72901-93ef-48f2-b11a-dd204e9eec31
                String fileName = imageIdMap.get(imageId); // ex) 강동구_애견동반식당_담금_thumbnail_5

                imageUrlMap.put(fileName, imageUrl);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("[getImageUrlFromFigma] " + e.getMessage());
        }

        return imageUrlMap;
    }


    private void processImages(Map<String, String> imageMap, Map<String, ArrayList<String>> listMap) {
        for (String fileName : imageMap.keySet()) {
            if (StringUtils.isNotEmpty(imageMap.get(fileName))) {
                System.out.println("  fileName: " + fileName);
                String image = imageMap.get(fileName);
                String webpFileName = photoService.convertWebpFromUrl(fileName, image);

                try {
                    String type = checkType(fileName);
                    BucketName bucketName = BucketName.fromFigma(type);
                    objectStorageService.uploadObjects(bucketName, webpFileName, DIR + webpFileName);
                    String savedImage = bucketName.getUrl() + webpFileName;

                    listMap.get(type).add(savedImage);
                } catch (IllegalArgumentException e) {
                    log.error("Failed to determine BucketName for file: " + webpFileName, e);
                }
            }
        }
    }

    private String checkType(String text) {
        String[] split = text.split("_");
        if (split.length == 6)
            return "menu_board";

        return switch (split[3]) {
            case "thumbnail" -> "thumbnail";
            case "menu" -> "menu";
            case "price" -> "price";
            case "dog" -> "dog";
            default -> "";
        };
    }
}
