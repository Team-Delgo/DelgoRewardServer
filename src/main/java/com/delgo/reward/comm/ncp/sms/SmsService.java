package com.delgo.reward.comm.ncp.sms;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class SmsService {
    String requestUrlHeader = "https://sens.apigw.ntruss.com";    	// 요청 URL
    String requestUrlService = "/sms/v2/services/";
    String requestUrlType = "/messages";                      		// 요청 URL
    String accessKey = "CU54eUVGT4dRhR7H1ocm";                     						// 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키
    String secretKey = "37b5b1d6838546b48813688660df7557";  										// 2차 인증을 위해 서비스마다 할당되는 service secret
    String sigSecretKey = "oCzPFBWmPMFYCf6Z9FU6iMMBtXB1RR7UdGV2BZuS";
    String serviceId = "ncp:sms:kr:271788577003:delgo-sms";        									// 프로젝트에 할당된 SMS 서비스 ID
    String method = "POST";											// 요청 method
    String sendFrom = "07079542910";
    String apiUrl = requestUrlHeader + requestUrlService + serviceId + requestUrlType;
    String sigUrl = requestUrlService + serviceId + requestUrlType;


    public SmsResponseDTO sendSMS(String recipientPhoneNumber, String content) throws JsonProcessingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, URISyntaxException {
        Long time = System.currentTimeMillis();
        String timeStamp = time.toString();
        List<SmsMessageDTO> messages = new ArrayList<>();
        messages.add(new SmsMessageDTO(recipientPhoneNumber, content));
        SmsRequestDTO smsRequestDTO = new SmsRequestDTO("SMS", "COMM", "82", sendFrom, "Delgo", messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(smsRequestDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json; charset=utf-8"));
        headers.set("x-ncp-apigw-timestamp", timeStamp);
        headers.set("x-ncp-iam-access-key", accessKey);
        String sig = makeSignature(timeStamp);
        headers.set("x-ncp-apigw-signature-v2", sig);
        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            public boolean hasError(ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                System.out.println("SendSMS StatusCode: " + statusCode);
                if(statusCode.series() != HttpStatus.Series.SUCCESSFUL) {
                    throw new IOException();
                }
                return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
            }
        });
        SmsResponseDTO smsResponseDTO = restTemplate.postForObject(apiUrl, body, SmsResponseDTO.class);
        System.out.println("SendSmsResponseDTO: " + smsResponseDTO);
        return smsResponseDTO;
    }
    public String makeSignature(String time) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String space = " ";
        String newLine = "\n";
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(sigUrl)
                .append(newLine)
                .append(time)
                .append(newLine)
                .append(accessKey)
                .toString();
        SecretKeySpec signingKey = new SecretKeySpec(sigSecretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.encodeBase64String(rawHmac);
        return encodeBase64String;
    }
}


