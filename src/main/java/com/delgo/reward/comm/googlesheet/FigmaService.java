package com.delgo.reward.comm.googlesheet;

import com.delgo.reward.comm.exception.FigmaException;
import com.delgo.reward.comm.ncp.storage.BucketName;
import com.delgo.reward.mungple.domain.Mungple;
import com.delgo.reward.common.service.PhotoService;
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

import java.io.UnsupportedEncodingException;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class FigmaService {
    private final PhotoService photoService;

    private final String API_URL = "https://api.figma.com/v1/";
    private final String figmaToken = "figd_r19ArRmULsFDOcl1Mim1B7zpphHYgqYM-YT84yfI";
    private final String figmaFileKey = "yzrVwMDiG6uU8LM57RUfN0";

    @Value("${config.photo-dir}")
    String DIR;

    public void uploadFigmaDataToNCP(String nodeId, Mungple mungple) {
        try {
            // Figma 연동 Data 조회
            Map<String, String> imageIdMap = getImageIdFromFigma(nodeId, mungple.makeBaseNameForFigma());
            Map<String, String> imageUrlMap = getImageUrlFromFigma(imageIdMap);

            // typeListMap 초기화
            Map<String, ArrayList<String>> typeListMap = Map.of(
                    "", new ArrayList<>(), // thumbnail
                    "menu", new ArrayList<>(),
                    "menu_board", new ArrayList<>(),
                    "price", new ArrayList<>(),
                    "dog", new ArrayList<>()
            );

            // imageUrlMap을 Type에 맞게 각 List에 저장
            uploadImages(imageUrlMap, typeListMap);

            // typeList를 각 Fileds에 매치 후 저장
            mungple.setFigmaPhotoData(typeListMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new FigmaException(e.getMessage());
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

    public Map<String, String> getImageIdFromFigma(String nodeId, String baseName) {
        RestTemplate restTemplate = createRestTemplate();
        HttpHeaders headers = createHeaders();

        String requestURL = API_URL + "files/" + figmaFileKey + "/nodes?ids=" + nodeId;
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Map<String, String> imageIdMap = new HashMap<>();
        try {
            JsonNode rootNode = new ObjectMapper().readTree(responseEntity.getBody());
            JsonNode childrNodes = rootNode.get("nodes").get(nodeId).get("document").get("children");

            for (JsonNode childNode : childrNodes) {
                String imageId = childNode.get("id").asText(); // ex) 4935:43532
                String figmaFileName = childNode.get("name").asText().replaceAll("\\s+", "");  // ex) 담금_menu_5
                int order = getOrderByFileName(figmaFileName); // ex) 5
                String type = getTypeByFileName(figmaFileName); // ex) menu
                String fileName = (type.isBlank()) ? baseName + "_" + order : baseName + "_" + type + "_" + order; // ex) 강동구_애견동반식당_담금_menu_5

                imageIdMap.put(imageId, fileName);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new FigmaException(e.getMessage());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            throw new FigmaException("figmaNodeId를 확인해주세요");
        }

        return imageIdMap;
    }

    public Map<String, String> getImageUrlFromFigma(Map<String, String> imageIdMap) {
        RestTemplate restTemplate = createRestTemplate();
        HttpHeaders headers = createHeaders();

        String requestURL = API_URL + "images/" + figmaFileKey + "?format=png&scale=3&ids=" + String.join(",", imageIdMap.keySet());
        ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        Map<String, String> imageUrlMap = new HashMap<>();
        try {
            JsonNode rootNode = new ObjectMapper().readTree(responseEntity.getBody());
            JsonNode imageNodes = rootNode.get("images");
            Iterator<Map.Entry<String, JsonNode>> fields = imageNodes.fields();
            while (fields.hasNext()) { // Iterator<Map.Entry<String, JsonNode>>
                Map.Entry<String, JsonNode> field = fields.next();
                String imageId = field.getKey(); // ex) 4931:43548
                String imageUrl = field.getValue().asText(); // ex) https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/8ab72901-93ef-48f2-b11a-dd204e9eec31
                String fileName = imageIdMap.get(imageId); // ex) 강동구_애견동반식당_담금_menu_5

                imageUrlMap.put(fileName, imageUrl);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new FigmaException(e.getMessage());
        }

        return imageUrlMap;
    }

    private void uploadImages(Map<String, String> imageMap, Map<String, ArrayList<String>> typeListMap) throws UnsupportedEncodingException {
        for (String fileName : imageMap.keySet()) {
            if (StringUtils.isNotEmpty(imageMap.get(fileName))) {
                String imageUrl = imageMap.get(fileName);
                String type = getTypeByFileName(fileName);
                BucketName bucketName = BucketName.fromFigma(type);

                String uploadedUrl = photoService.downloadAndUploadFromURL(fileName, imageUrl, bucketName);
                typeListMap.get(type).add(uploadedUrl);
            }
        }
    }

    private String getTypeByFileName(String fileName) {
        try {
            String[] type_arr = fileName.split("_");
            String type = type_arr[type_arr.length - 2];

            return switch (type) {
                case "board" -> "menu_board";
                case "menu" -> "menu";
                case "price" -> "price";
                case "dog" -> "dog";
                default -> "";
            };
        } catch (Exception e) {
            throw new FigmaException(" [Figma] : " + fileName + " 확인해주세요");
        }
    }

    private int getOrderByFileName(String fileName) {
        try {
            String[] type_arr = fileName.split("_");
            return Integer.parseInt(type_arr[type_arr.length - 1]);
        } catch (Exception e) {
            throw new FigmaException(" [Figma] : " + fileName + " 확인해주세요");
        }
    }
}
