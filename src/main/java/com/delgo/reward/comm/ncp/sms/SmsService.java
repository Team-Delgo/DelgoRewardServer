package com.delgo.reward.comm.ncp.sms;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
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
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Getter
@Component
public class SmsService {
    @Value("${ncp.sms.url}")
    String API_URL;
    @Value("${ncp.sms.signature-path}")
    String SIGNATURE_PATH;
    @Value("${ncp.sms.access-key}")
    String ACCESS_KEY;
    @Value("${ncp.sms.secret-key}")
    String SECRET_KEY;

    public boolean send(String recipientPhoneNumber, String content) {
        try {
            SmsRequest smsRequest = SmsRequest.from(recipientPhoneNumber, content);
            String jsonBody = new ObjectMapper().writeValueAsString(smsRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/json; charset=utf-8"));
            headers.set("x-ncp-apigw-timestamp", Long.toString(System.currentTimeMillis()));
            headers.set("x-ncp-iam-access-key", ACCESS_KEY);
            headers.set("x-ncp-apigw-signature-v2", getSignature());

            HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);

            RestTemplate restTemplate = getRestTemplate();
            SmsResponse smsResponse = restTemplate.postForObject(API_URL, body, SmsResponse.class);

            assert smsResponse != null;
            return smsResponse.getStatusCode().equals("202");
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @NotNull
    private static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            public boolean hasError(@NotNull ClientHttpResponse response) throws IOException {
                HttpStatus statusCode = response.getStatusCode();
                System.out.println("SendSMS StatusCode: " + statusCode);
                if (statusCode.series() != HttpStatus.Series.SUCCESSFUL) {
                    throw new IOException();
                }
                return false;
            }
        });
        return restTemplate;
    }

    public String getSignature() {
        try {
            String message = String.format("POST %s\n%s\n%s", SIGNATURE_PATH, System.currentTimeMillis(), ACCESS_KEY);
            SecretKeySpec signingKey = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(rawHmac);

        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            return "";
        }
    }
}


