package com.delgo.reward.certification.infrastructure;

import com.delgo.reward.certification.service.port.GeoDataPort;
import com.delgo.reward.domain.common.Location;
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

    public JsonNode connect(String requestURL) {
        JsonNode jsonNode = null;
        try {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
            headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, entity, String.class);

            jsonNode = new ObjectMapper().readTree(responseEntity.getBody());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return jsonNode;
    }

    @Override // TODO: 나중에 적용
    public Location getGeoData(String address) {
        String requestURL = API_GEO_URL + "?query=" + address;
        JsonNode jsonNode = connect(requestURL);

        return new Location();
    }

    @Override
    public String getReverseGeoData(String latitude, String longitude) {
        String requestURL = API_REVERSE_GEO_URL + "?coords=" + longitude + "," + latitude + "&output=json";

        JsonNode jsonNode = connect(requestURL);
        JsonNode regionNode = jsonNode.path("results").path(0).path("region");

        return String.join(" ",
                getTextFromJsonNode(regionNode, "area1"), // 시,도
                getTextFromJsonNode(regionNode, "area2"), // 시,구,군
                getTextFromJsonNode(regionNode, "area3")); // 동
    }

    private String getTextFromJsonNode(JsonNode jsonNode, String fieldName) {
        return Optional.ofNullable(jsonNode.path(fieldName))
                .map(node -> node.path("name"))
                .map(JsonNode::asText)
                .orElse("");
    }
}
