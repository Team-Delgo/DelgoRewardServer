package com.delgo.reward.ncp.infrastructure;



import com.delgo.reward.ncp.domain.SmsMessage;
import com.delgo.reward.ncp.domain.SmsRequest;
import com.delgo.reward.ncp.service.port.SmsPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;


@Component
public class SmsAdapter implements SmsPort {
    String REQUEST_HOST = "https://sens.apigw.ntruss.com";    	// 요청 URL
    String REQUEST_PATH = "/sms/v2/services/";
    String SERVICE_ID = "ncp:sms:kr:271788577003:delgo-sms";
    String URL_TYPE = "/messages"; // 요청 URL

    String ACCESS_KEY = "CU54eUVGT4dRhR7H1ocm"; // 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키
    String SECRET_KEY = "37b5b1d6838546b48813688660df7557"; // 2차 인증을 위해 서비스마다 할당되는 service secret
    String SIG_SECRET_KEY = "oCzPFBWmPMFYCf6Z9FU6iMMBtXB1RR7UdGV2BZuS";

    String API_URL = REQUEST_HOST + REQUEST_PATH + SERVICE_ID + URL_TYPE;
    String SIG_URL = REQUEST_PATH + SERVICE_ID + URL_TYPE;


    @Override
    public void send(String phone, String msg) {
        try {
            String timeStamp = Long.toString(System.currentTimeMillis());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/json; charset=utf-8"));
            headers.set("x-ncp-apigw-timestamp", timeStamp);
            headers.set("x-ncp-iam-access-key", ACCESS_KEY);
            headers.set("x-ncp-apigw-signature-v2", makeSignature(timeStamp));

            SmsRequest smsRequest = SmsRequest.create(List.of(new SmsMessage(phone, msg)));
            String jsonBody = new ObjectMapper().writeValueAsString(smsRequest);

            HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();

            String smsResponse = restTemplate.postForObject(API_URL, body, String.class);
            System.out.println("smsResponse = " + smsResponse);
        } catch (Exception e){
            System.err.println("sendSMS An error occurred: " + e.getMessage());
        }
    }

    private String makeSignature(String time) throws InvalidKeyException, NoSuchAlgorithmException {
        String message = "POST" + " " + SIG_URL + "\n" + time + "\n" + ACCESS_KEY;
        SecretKeySpec signingKey = new SecretKeySpec(SIG_SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(rawHmac);
    }
}


