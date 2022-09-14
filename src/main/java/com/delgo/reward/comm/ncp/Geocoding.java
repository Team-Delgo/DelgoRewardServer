package com.delgo.reward.comm.ncp;

import com.delgo.reward.comm.CommController;
import com.delgo.reward.comm.ncp.dto.GeoResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Geocoding extends CommController {
    private static final String CLIENT_ID = "a8lt0yd9uy";
    private static final String CLIENT_SECRET = "P1WuQqH2d7rAnbWraxGwgDjPVvayuFwhV0RQAXtR";
//    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=";

    @GetMapping("/geocoding")
    public ResponseEntity<?> getGeoData(@RequestParam String address){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        HttpHeaders headers  = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
        headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);

        String encodedAddress = null;
        try {
            encodedAddress = URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String requestUrl = API_URL + encodedAddress;

        HttpEntity entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            System.out.println("status code: " + responseEntity.getStatusCode());
//            System.out.println("json node: " + jsonNode);

            System.out.println("response: " + responseEntity);
            System.out.println("response body: " + responseEntity.getBody());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }



        return SuccessReturn(responseEntity.getBody());

//        String param = "query=" + query;
//
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.set("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
//        headers.set("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
//
//        HttpEntity<String> body = new HttpEntity<>(param, headers);
//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
//            public boolean hasError(ClientHttpResponse response) throws IOException {
//                HttpStatus statusCode = response.getStatusCode();
//
//                System.out.println("StatusCode: " + statusCode);
//                System.out.println("Body: " + response.getBody());
//
//                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
//            }
//        });
//        GeoResponseDTO geoResponseDTO = restTemplate.getForObject(URL, GeoResponseDTO.class);
//        System.out.println(geoResponseDTO.getGeoAddressDTO().getRoadAddress());
//        return SuccessReturn();
    }



//        try {
//            HttpClient client = HttpClientBuilder.create().build();
//            HttpGet getRequest = new HttpGet(URL);
//            getRequest.setHeader("X-NCP-APIGW-API-KEY-ID", CLIENT_ID);
//            getRequest.setHeader("X-NCP-APIGW-API-KEY", CLIENT_SECRET);
//
//            HttpResponse response = client.execute(getRequest);
//
//            //Response 출력
//            if (response.getStatusLine().getStatusCode() == 200) {
//                ResponseHandler<String> handler = new BasicResponseHandler();
//                String body = handler.handleResponse(response);
//
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode bodyJson = mapper.readTree(body);
//
//                System.out.println(bodyJson);
//                System.out.println("Status code: " + response.getStatusLine().getStatusCode());
//
//                return SuccessReturn(bodyJson);
//            }
//
//            System.out.println("Status code: " + response.getStatusLine().getStatusCode());
//
//            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
//        } catch (Exception e) {
//            return ErrorReturn(ApiCode.UNKNOWN_ERROR);
//        }
}
