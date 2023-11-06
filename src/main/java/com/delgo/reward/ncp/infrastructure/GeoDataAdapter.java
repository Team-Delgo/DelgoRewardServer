package com.delgo.reward.ncp.infrastructure;

import com.delgo.reward.domain.common.GeoData;
import com.delgo.reward.ncp.service.port.GeoDataPort;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeoDataAdapter implements GeoDataPort {
    private static final String API_GEO_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private static final String API_REVERSE_GEO_URL = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc";
    private static final String CLIENT_ID = "a8lt0yd9uy";
    private static final String CLIENT_SECRET = "P1WuQqH2d7rAnbWraxGwgDjPVvayuFwhV0RQAXtR";

    @Override
    public GeoData getGeoData(String address) {
        String requestURL = API_GEO_URL + "?query=" + address;

        JsonNode jsonNode = connect(requestURL);
        JsonNode addressesNode = jsonNode.get("addresses").get(0);

        return GeoData.builder()
                .latitude(getDirectTextFromJsonNode(addressesNode, "y"))
                .longitude(getDirectTextFromJsonNode(addressesNode, "x"))
                .roadAddress(getDirectTextFromJsonNode(addressesNode, "roadAddress"))
                .jibunAddress(getDirectTextFromJsonNode(addressesNode, "jibunAddress"))
                .build();
    }

    @Override
    public String getReverseGeoData(String latitude, String longitude) {
        String requestURL = API_REVERSE_GEO_URL + "?coords=" + longitude + "," + latitude + "&output=json";

        JsonNode jsonNode = connect(requestURL);
        JsonNode regionNode = jsonNode.path("results").path(0).path("region");

        return String.join(" ",
                getNestedTextFromJsonNode(regionNode, "area1"), // 시,도
                getNestedTextFromJsonNode(regionNode, "area2"), // 시,구,군
                getNestedTextFromJsonNode(regionNode, "area3")); // 동
    }

    public JsonNode connect(String requestURL) {
        try {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
            headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

            return new ObjectMapper().readTree(responseEntity.getBody());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private String getDirectTextFromJsonNode(JsonNode jsonNode, String fieldName) {
        return Optional.ofNullable(jsonNode.get(fieldName))
                .map(JsonNode::asText)
                .orElse("");
    }

    private String getNestedTextFromJsonNode(JsonNode jsonNode, String fieldName) {
        return Optional.ofNullable(jsonNode.path(fieldName))
                .map(node -> node.path("name"))
                .map(JsonNode::asText)
                .orElse("");
    }
}
